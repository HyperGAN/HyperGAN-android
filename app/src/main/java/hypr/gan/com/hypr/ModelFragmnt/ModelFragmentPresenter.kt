package hypr.gan.com.hypr.ModelFragmnt

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.pawegio.kandroid.start
import hypr.gan.com.hypr.Generator.Generator
import hypr.gan.com.hypr.GeneratorLoader.EasyGeneratorLoader
import hypr.gan.com.hypr.GeneratorLoader.FaceLocation
import hypr.gan.com.hypr.GeneratorLoader.Person
import hypr.gan.com.hypr.Main.MainActivity
import hypr.gan.com.hypr.R
import hypr.gan.com.hypr.Util.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.io.File
import java.io.IOException
import java.lang.IllegalArgumentException
import kotlin.properties.Delegates


class ModelFragmentPresenter(val easyGenerator: EasyGeneratorLoader) : ModelFragmentMVP.presenter {


    private var imageDisplayedOnScreen: Bitmap? = null
    val SHARE_IMAGE_PERMISSION_REQUEST = 10
    val SAVE_IMAGE_PERMISSION_REQUEST: Int = 11
    var generator: Generator by Delegates.observable(Generator()) { property, oldValue, newValue ->
        newValue.let { easyGenerator.loadGenerator(newValue) }
        newValue
    }
    var generatorIndex: Int? = null
    lateinit var person: Person
    lateinit var view: ModelFragmentMVP.view
    lateinit var interactor: ModelInteractor
    private var imageManipulatedFromzValue: Bitmap? = null
    var loadGeneratorLauncher: Job? = null

    fun loadGenerator(context: Context, pbFile: File?) {
        loadGeneratorLauncher = launch(UI) {
            val imageBitmap: Deferred<Bitmap?> = bg {
                loadGenerator(pbFile, context.assets)
                val bitmap = person.fullImage.toBitmap()
                val faces = getFaceCroppedOutOfImageIfNoFaceGetFullImage(bitmap, context)
                return@bg sampleImage(person, faces, interactor.settings.getFaceLocation())
            }
            try {
                val image = imageBitmap.await()
                if (image != null) {
                    view.displayFocusedImage(image)
                }

            } catch (e: IllegalArgumentException) {
                println("bitmap null")
                e.printStackTrace()
                ImageSaver().deleteImagesFromFragment()
                SettingsHelper(context).resetImagePaths()
                context.intentFor<MainActivity>().clearTop().start(context)
//                view.finishActivity()
                println("imagebitmap null end ")
                context.toast("There was an error..")

            }
        }
        generatorIndex?.let { easyGenerator.setIndex(it) }
    }

    fun setViews(view: ModelFragmentMVP.view) {
        this.view = view
    }

    fun setInteractors(interactor: ModelInteractor) {
        this.interactor = interactor
    }

    fun openRateAppInPlayStore(packageName: String?) {
        val marketLink = Uri.parse("market://details?id=$packageName")
        val playStoreLink = Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
        view.openRateAppInPlayStore(marketLink, playStoreLink)


    }

    override fun disconnectFaceDetector() {
        interactor.faceDetection.release()
    }

    override fun readImageToBytes(imagePath: String?): ByteArray? {
        return imagePath?.let { File(it).readBytes() }
    }

    fun shareImageToOtherApps() {
        if (interactor.checkIfPermissionGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val watermarkBitmap = interactor.placeWatermarkOnImage(imageDisplayedOnScreen)
            val shareIntent = interactor.getIntentForSharingImagesWithOtherApps(watermarkBitmap)
            view.shareImageToOtherApps(shareIntent)
        } else {
            view.requestPermissionFromUser(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), SHARE_IMAGE_PERMISSION_REQUEST)
        }
    }

    override fun randomizeModel(progress: Int) {
        easyGenerator.direction = easyGenerator.random_z()

        easyGenerator.encoded = easyGenerator.random_z()

        changeGanImageFromSlider(progress.negative1To1())
    }

    override fun getFaceCroppedOutOfImageIfNoFaceGetFullImage(imageWithFaces: Bitmap?, context: Context): Bitmap? {
        var image: Bitmap? = null
        try {
            image = if (imageWithFaces == null) {
                imageWithFaces
            } else {
                getCroppedFaceImagFromImageWithFaces(imageWithFaces, context)
            }
        } catch (exception: IOException) {
            Log.e("ModelFragment", exception.message)
        }
        return image
    }

    private fun getCroppedFaceImagFromImageWithFaces(imageWithFaces: Bitmap, context: Context): Bitmap? {
        val croppedFaces: MutableList<FaceLocation> = interactor.getFacesFromBitmap(imageWithFaces, imageWithFaces.width, imageWithFaces.height, context)
        val faceImage = if (isFacesDetected(croppedFaces)) {
            val faceIndex = interactor.settings.getFaceIndex()
            croppedFaces[faceIndex].croppedFace
        } else {
            imageWithFaces
        }
        return faceImage
    }

    fun isFacesDetected(listOfFaces: MutableList<FaceLocation>): Boolean {
        return !listOfFaces.isEmpty()
    }

    override fun saveImageDisplayedToPhone(context: Context): Boolean {
        var isSaved = false
        if (interactor.checkIfPermissionGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val faceImage = person.faceImage?.toBitmap()
            if (imageDisplayedOnScreen != null) {
                val inlineImage = inlineImage(person, imageDisplayedOnScreen!!)
                val waterMarkImage = interactor.placeWatermarkOnImage(inlineImage)
                isSaved = ImageSaver().saveImageToInternalStorage(waterMarkImage, context)

            } else {
                isSaved = ImageSaver().saveImageToInternalStorage(imageDisplayedOnScreen, context)
                val imageCopy = faceImage?.copy(faceImage.config, true)
                isSaved = ImageSaver().saveImageToInternalStorage(imageCopy, context)
            }
        } else {
            view.requestPermissionFromUser(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), SAVE_IMAGE_PERMISSION_REQUEST)
        }
        return isSaved
    }

    fun inlineImage(person: Person, newCroppedImage: Bitmap): Bitmap? {
        val faceImage = person.faceImage?.toBitmap()
        val image: Bitmap? = if (faceImage != null) {
            easyGenerator.inlineImage(person, newCroppedImage, interactor.settings.getFaceLocation())
        } else {
            newCroppedImage
        }
        return image
    }

    fun loadGenerator(pbFile: File?, assets: AssetManager) {
        pbFile?.let { easyGenerator.load(assets, it) }
    }

    override fun sampleImage(person: Person, image: Bitmap?, croppedPoint: Rect): Bitmap? {
        val transformedImage = if (image != null) {
            easyGenerator.sampleImageWithImage(person, image, croppedPoint)
        } else {
            easyGenerator.sampleImageWithoutImage().toBitmap(easyGenerator.width, easyGenerator.height)
        }
        imageDisplayedOnScreen = transformedImage
        return transformedImage
    }

    override fun changePixelToBitmap(transformedImage: IntArray): Bitmap? {
        return transformedImage.toBitmap(easyGenerator.width, easyGenerator.height)
    }

    override fun onRequestPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray, context: Context) {
        grantResults.filter { item -> item == PackageManager.PERMISSION_GRANTED }.forEach { item ->
            if (requestCode == SHARE_IMAGE_PERMISSION_REQUEST) {
                shareImageToOtherApps()
            } else if (requestCode == SAVE_IMAGE_PERMISSION_REQUEST) {
                val coroutineContext = context
                launch(UI) {
                    bg { saveImageDisplayedToPhone(coroutineContext) }.await()
                    rateApp()
                }
            }
        }
    }

    private fun rateApp() {
        view.rateApp()
    }


    override fun onOptionsItemSelected(item: MenuItem, context: Context) {
        launch(UI) {
            when (item.itemId) {
                R.id.saveImage -> {
                    bg { saveImageDisplayedToPhone(context) }.await()
                    interactor.analytics.logEvent(AnalyticsEvent.SAVE_IMAGE)
                    context.toast(context.getString(R.string.image_saved_toast))
                }
                R.id.shareIamge -> {
                    rateApp()
                    shareImageToOtherApps()
                    interactor.analytics.logEvent(AnalyticsEvent.SHARE_IMAGE)
                }
            }
        }
    }

    fun startCameraActivity() {
        view.startCameraActivity()
    }

    fun getGeneratorImage(ganValue: Double): IntArray {
        val ganImage = easyGenerator.sampleImageWithZValue(ganValue.toFloat())
        return ganImage
    }

    fun manipulateZValueInImage(ganValue: Double): IntArray {
        val ganImage = getGeneratorImage(ganValue)
        return ganImage
    }

    fun changeGanImageFromSlider(ganValue: Double) {
        async(UI) {

            val imageManipluatedFromZValue = manipulateZValueInImage(ganValue)
            val imagePlacedInsideFullImage = bg {
                val ganImage = imageManipluatedFromZValue.toBitmap(easyGenerator.width, easyGenerator.height)
                imageManipulatedFromzValue = ganImage
                ganImage.let { inlineImage(person, it) }
            }
            view.displayFocusedImage(imagePlacedInsideFullImage.await())
        }
    }

    fun getInfoFromFragmentCreation(arguments: Bundle) {
        generator = arguments.getParcelable(ModelFragment.MODEL_CONTROLS)
        val faceImage = readImageToBytes(arguments.getString(ModelFragment.IMAGE_PARAM))
        generatorIndex = arguments.getInt(ModelFragment.GENERATOR_INDEX)
        val fullImage: String? = arguments.getString(ModelFragment.FULL_IMAGE_LOCATION)
        val fullImageBit = if (fullImage != null) {
            File(fullImage).readBytes()
        } else {
            easyGenerator.sampleImageWithoutImage().toByteArrayImage()
        }
        this.person = Person(faceImage, fullImageBit)
    }
}
