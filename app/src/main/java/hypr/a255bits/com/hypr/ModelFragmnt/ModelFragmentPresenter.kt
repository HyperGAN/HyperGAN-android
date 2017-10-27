package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.view.MenuItem
import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.GeneratorLoader.EasyGeneratorLoader
import hypr.a255bits.com.hypr.GeneratorLoader.FaceLocation
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
                convertByteArrayImageToBitmap()
            }
            transformImage(imageBitmap.await())
        }
    }

    val analytics = Analytics(context)
    var imageFromGallery: IntArray? = null
    val SHARE_IMAGE_PERMISSION_REQUEST = 10
    val SAVE_IMAGE_PERMISSION_REQUEST: Int = 11
    lateinit var easyGenerator: EasyGeneratorLoader
    var generator: Generator by Delegates.observable(Generator()) { property, oldValue, newValue ->
        newValue.generator?.let { easyGenerator = EasyGeneratorLoader(it) }
        newValue
    }
    var byteArrayImage: ByteArray? = null
    var fullImage: ByteArray? = null
    var generatorIndex: Int? = null
    var direction: FloatArray? = null

    override fun disconnectFaceDetector() {
        interactor.faceDetection.release()
    }

    override fun readImageToBytes(imagePath: String?) {
        byteArrayImage = imagePath?.let { File(it).readBytes() }
    }

    fun shareImageToOtherApps() {
        if (interactor.checkIfPermissionGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val bitmap = imageFromGallery?.let { changePixelToBitmap(it) }
            val shareIntent = interactor.getIntentForSharingImagesWithOtherApps(bitmap)
            view.shareImageToOtherApps(shareIntent)
        } else {
            view.requestPermissionFromUser(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), SHARE_IMAGE_PERMISSION_REQUEST)
        }
    }

    override fun randomizeModel(progress: Int) {
        view.changeGanImageFromSlider(progress.negative1To1())
    }

    override fun findFacesInImage(imageWithFaces: Bitmap?, context: Context) {
        try {
            launch(UI) {
                if (imageWithFaces == null) {
                    view.displayFocusedImage(imageWithFaces)
                } else {
                    val croppedFaces: MutableList<FaceLocation> = interactor.getFacesFromBitmap(imageWithFaces, imageWithFaces.width, imageWithFaces.height, context)
                    if (isFacesDetected(croppedFaces)) {
                        view.displayFocusedImage(croppedFaces[0].croppedFace)
                    } else {
                        view.displayFocusedImage(imageWithFaces)
                    }
                }
            }
        } catch (exception: IOException) {
            view.showError(exception.localizedMessage)
        }
    }

    fun isFacesDetected(listOfFaces: MutableList<FaceLocation>): Boolean {
        return !listOfFaces.isEmpty()
    }

    override fun saveImageDisplayedToPhone(context: Context): Boolean {
        var isSaved = false
        if (interactor.checkIfPermissionGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            var bitmap = imageFromGallery?.let { changePixelToBitmap(it) }
            val croppedPoint = SettingsHelper(context).getFaceLocation()
            if (bitmap != null && fullImage != null) {
                val inliner = InlineImage()
                inliner.setBeforeAfterCropSizingRatio(byteArrayImage?.toBitmap()!!, bitmap)
                val inlineImage = fullImage?.toBitmap()?.let { inliner.inlineCroppedImageToFullImage(bitmap!!, it, croppedPoint) }
                println("hello")
                isSaved = ImageSaver().saveImageToInternalStorage(inlineImage, context)

            }else{
                isSaved = ImageSaver().saveImageToInternalStorage(bitmap, context)
            }
        } else {
            view.requestPermissionFromUser(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), SAVE_IMAGE_PERMISSION_REQUEST)
        }
        return isSaved
    }

    override fun transformImage(normalImage: Bitmap?) {
        findFacesInImage(normalImage, context)
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
        imageFromGallery = transformedImage
        return easyGenerator.manipulateBitmap(easyGenerator.width, easyGenerator.height, transformedImage)
    }

    override fun changePixelToBitmap(transformedImage: IntArray): Bitmap? {
        return easyGenerator.manipulateBitmap(easyGenerator.width, easyGenerator.height, transformedImage)
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

    override fun convertByteArrayImageToBitmap(): Bitmap? {
        return byteArrayImage?.let { BitmapManipulator().createBitmapFromByteArray(it) }
    }

}
