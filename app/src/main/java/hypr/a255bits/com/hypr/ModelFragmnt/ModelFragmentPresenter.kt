package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.GeneratorLoader.EasyGeneratorLoader
import hypr.a255bits.com.hypr.GeneratorLoader.FaceLocation
import hypr.a255bits.com.hypr.GeneratorLoader.Person
import hypr.a255bits.com.hypr.R
import hypr.a255bits.com.hypr.Util.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.toast
import java.io.File
import java.io.IOException
import kotlin.properties.Delegates


class ModelFragmentPresenter(val view: ModelFragmentMVP.view, val interactor: ModelInteractor, val context: Context, pbFile: File?) : ModelFragmentMVP.presenter {

    init {
        val cont = context
        launch(UI) {
            val imageBitmap = bg {
                loadGenerator(pbFile, cont.assets)
                val bitmap = person.faceImage?.toBitmap()
                val faces = getFaceCroppedOutOfImageIfNoFaceGetFullImage(bitmap, cont)
                val transformedImage: Bitmap = sampleImage(faces)
                return@bg inlineImage(person, transformedImage)
            }
            view.displayFocusedImage(imageBitmap.await())
        }
    }

    val analytics = Analytics(context)
    var imageDisplayedOnScreen: IntArray? = null
    val SHARE_IMAGE_PERMISSION_REQUEST = 10
    val SAVE_IMAGE_PERMISSION_REQUEST: Int = 11
    lateinit var easyGenerator: EasyGeneratorLoader
    var generator: Generator by Delegates.observable(Generator()) { property, oldValue, newValue ->
        newValue.generator?.let { easyGenerator = EasyGeneratorLoader(it) }
        newValue
    }
    var generatorIndex: Int? = null
    var direction: FloatArray? = null
    val croppedPoint = SettingsHelper(context).getFaceLocation()
    val inliner = InlineImage()
    lateinit var person: Person

    override fun disconnectFaceDetector() {
        interactor.faceDetection.release()
    }

    override fun readImageToBytes(imagePath: String?): ByteArray? {
        return imagePath?.let { File(it).readBytes() }
    }

    fun shareImageToOtherApps() {
        if (interactor.checkIfPermissionGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val bitmap = imageDisplayedOnScreen?.let { changePixelToBitmap(it) }
            val watermarkBitmap = interactor.placeWatermarkOnImage(bitmap)
            val shareIntent = interactor.getIntentForSharingImagesWithOtherApps(watermarkBitmap)
            view.shareImageToOtherApps(shareIntent)
        } else {
            view.requestPermissionFromUser(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), SHARE_IMAGE_PERMISSION_REQUEST)
        }
    }

    override fun randomizeModel(progress: Int) {
        changeGanImageFromSlider(progress.negative1To1())
    }

    override fun getFaceCroppedOutOfImageIfNoFaceGetFullImage(imageWithFaces: Bitmap?, context: Context): Bitmap? {
        var image: Bitmap? = null
        try {
            image = if (imageWithFaces == null) {
                imageWithFaces
            } else {
                getCroppedFaceImagFromImageWithFaces(imageWithFaces)
            }
        } catch (exception: IOException) {
            view.showError(exception.localizedMessage)
        }
        return image
    }

    private fun getCroppedFaceImagFromImageWithFaces(imageWithFaces: Bitmap): Bitmap? {
        val croppedFaces: MutableList<FaceLocation> = interactor.getFacesFromBitmap(imageWithFaces, imageWithFaces.width, imageWithFaces.height, context)
        val faceImage = if (isFacesDetected(croppedFaces)) {
            val faceIndex = SettingsHelper(context).getFaceIndex()
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
            var bitmap = imageDisplayedOnScreen?.let { changePixelToBitmap(it) }
            val croppedPoint = SettingsHelper(context).getFaceLocation()
            if (bitmap != null && person.fullImage != null) {
                val inlineImage = inlineImage(person, bitmap)
                val waterMarkImage = interactor.placeWatermarkOnImage(inlineImage)
                isSaved = ImageSaver().saveImageToInternalStorage(waterMarkImage, context)

            } else {
                isSaved = ImageSaver().saveImageToInternalStorage(bitmap, context)
            }
        } else {
            view.requestPermissionFromUser(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), SAVE_IMAGE_PERMISSION_REQUEST)
        }
        return isSaved
    }

    fun inlineImage(person: Person, newCroppedImage: Bitmap): Bitmap? {
        val fullImage = person.fullImage
        val faceImage = person.faceImage?.toBitmap()
        val image: Bitmap? = if (faceImage != null) {
            inliner.setBeforeAfterCropSizingRatio(faceImage, newCroppedImage)
            fullImage.toBitmap()?.let { inliner.inlineCroppedImageToFullImage(newCroppedImage, it, croppedPoint) }
        } else {
            newCroppedImage
        }
        return image
    }

    fun loadGenerator(pbFile: File?, assets: AssetManager) {
        pbFile?.let { easyGenerator.load(assets, it) }
    }

    override fun sampleImage(image: Bitmap?): Bitmap {
        val transformedImage = if (image != null) {
            easyGenerator.sampleImageWithImage(image)
        } else {
            easyGenerator.sampleImageWithoutImage()
        }
        imageDisplayedOnScreen = transformedImage
        return transformedImage.toBitmap(easyGenerator.width, easyGenerator.height)
    }

    override fun changePixelToBitmap(transformedImage: IntArray): Bitmap? {
        return transformedImage.toBitmap(easyGenerator.width, easyGenerator.height)
    }

    override fun onRequestPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        grantResults.filter { item -> item == PackageManager.PERMISSION_GRANTED }.forEach { item ->
            if (requestCode == SHARE_IMAGE_PERMISSION_REQUEST) {
                shareImageToOtherApps()
            } else if (requestCode == SAVE_IMAGE_PERMISSION_REQUEST) {
                val coroutineContext = context
                launch(UI) {
                    bg { saveImageDisplayedToPhone(coroutineContext) }.await()
                }
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem, context: Context) {
        launch(UI) {
            when (item.itemId) {
                R.id.saveImage -> {
                    bg { saveImageDisplayedToPhone(context) }.await()
                    analytics.logEvent(AnalyticsEvent.SAVE_IMAGE)
                    context.toast("Image Saved!")
                }
                R.id.shareIamge -> {
                    shareImageToOtherApps()
                    analytics.logEvent(AnalyticsEvent.SHARE_IMAGE)
                }
            }
        }
    }

    fun startCameraActivity() {
        view.startCameraActivity()
    }

    fun getGeneratorImage(ganValue: Double): IntArray {
        val direction = direction ?: easyGenerator.random_z()
        val ganImage = easyGenerator.sample(easyGenerator.encoded!!, ganValue.toFloat(), easyGenerator.mask, direction, easyGenerator.baseImage!!)
        imageDisplayedOnScreen = ganImage
        return ganImage
    }

    fun manipulateZValueInImage(ganValue: Double): Bitmap? {
        val ganImage = getGeneratorImage(ganValue)
        return ganImage.toBitmap(easyGenerator.width, easyGenerator.height)
    }

    fun changeGanImageFromSlider(ganValue: Double) {
        val imageManipluatedFromZValue = manipulateZValueInImage(ganValue)
        val imagePlacedInsideFullImage = imageManipluatedFromZValue?.let { inlineImage(person, it) }
        view.displayFocusedImage(imagePlacedInsideFullImage)
    }

    fun getInfoFromFragmentCreation(arguments: Bundle) {
        generator = arguments.getParcelable(ModelFragment.MODEL_CONTROLS)
        val faceImage = readImageToBytes(arguments.getString(ModelFragment.IMAGE_PARAM))
        generatorIndex = arguments.getInt(ModelFragment.GENERATOR_INDEX)
        val fullImage: File? = arguments.getString(ModelFragment.FULL_IMAGE_LOCATION).let { File(it) }
        val fullImageBit = fullImage?.readBytes() ?: easyGenerator.sampleImageWithoutImage().toByteArrayImage()
        this.person = Person(faceImage, fullImageBit)
    }
}
