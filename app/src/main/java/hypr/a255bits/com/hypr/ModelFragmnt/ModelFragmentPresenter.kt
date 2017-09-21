package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.view.MenuItem
import hypr.a255bits.com.hypr.Generator.Control
import hypr.a255bits.com.hypr.GeneratorLoader.GeneratorLoader
import hypr.a255bits.com.hypr.R
import hypr.a255bits.com.hypr.Util.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.toast
import java.io.File
import java.io.IOException


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
    val generatorLoader = GeneratorLoader()
    var modelUrl: Array<Control>? = null
    var byteArrayImage: ByteArray? = null
    var baseImage: Bitmap? = null
    var mask: FloatArray? = null
    var encoded: FloatArray? = null
    var generatorIndex: Int? = null
    var direction: FloatArray? = null

    override fun disconnectFaceDetector() {
        interactor.faceDetection.release()
    }

    override fun displayTitleSpinner() {

    }

    override fun readImageToBytes(imagePath: String?) {
        byteArrayImage = imagePath?.let {  File(it).readBytes()}
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

    override fun findFacesInImage(imageWithFaces: Bitmap, context: Context) {
        try {
            launch(UI){
            val croppedFaces: MutableList<Bitmap> = interactor.getFacesFromBitmap(imageWithFaces, imageWithFaces.width, imageWithFaces.height, context)
            if (isFacesDetected(croppedFaces)) {
                view.displayFocusedImage(croppedFaces[0])
            } else {
                view.displayFocusedImage(imageWithFaces)
            }
        }
        } catch (exception: IOException) {
            view.showError(exception.localizedMessage)
        }
    }

    fun isFacesDetected(listOfFaces: MutableList<Bitmap>): Boolean {
        return !listOfFaces.isEmpty()
    }

    override fun saveImageDisplayedToPhone(context: Context): Boolean {
        var isSaved = false
        if (interactor.checkIfPermissionGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val bitmap = imageFromGallery?.let { changePixelToBitmap(it) }
            isSaved = ImageSaver().saveImageToInternalStorage(bitmap, context)
        } else {
            view.requestPermissionFromUser(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), SAVE_IMAGE_PERMISSION_REQUEST)
        }
        return isSaved
    }

    override fun transformImage(normalImage: Bitmap?) {
        normalImage?.let { findFacesInImage(it, context) }
    }

    fun loadGenerator(pbFile: File?, assets: AssetManager) {
        pbFile?.let { generatorLoader.load(assets, it) }
    }

    override fun sampleImage(image: Bitmap): Bitmap {
        val scaled = Bitmap.createScaledBitmap(image, 256, 256, false)
        baseImage = scaled

        encoded = generatorLoader.encode(scaled)

        mask = generatorLoader.mask(scaled)
        val direction = generatorLoader.random_z()
        val transformedImage = generatorLoader.sample(encoded!!, 0.0f, mask, direction, scaled!!)
        imageFromGallery = transformedImage
        return generatorLoader.manipulateBitmap(generatorLoader.width, generatorLoader.height, transformedImage)
    }

    override fun changePixelToBitmap(transformedImage: IntArray): Bitmap? {
        return generatorLoader.manipulateBitmap(generatorLoader.width, generatorLoader.height, transformedImage)
    }

    override fun onRequestPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        grantResults.filter { item -> item == PackageManager.PERMISSION_GRANTED }.forEach { item ->
            if (requestCode == SHARE_IMAGE_PERMISSION_REQUEST) {
                shareImageToOtherApps()
            } else if (requestCode == SAVE_IMAGE_PERMISSION_REQUEST) {
                val con = context
                launch(UI) {
                    bg { saveImageDisplayedToPhone(con) }.await()
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
