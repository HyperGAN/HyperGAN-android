package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.view.MenuItem
import hypr.a255bits.com.hypr.Generator.Control
import hypr.a255bits.com.hypr.GeneratorLoader
import hypr.a255bits.com.hypr.R
import hypr.a255bits.com.hypr.Util.ImageSaver
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.toast
import java.io.File
import java.io.IOException


class ModelFragmentPresenter(val view: ModelFragmentMVP.view, val interactor: ModelInteractor, val context: Context) : ModelFragmentMVP.presenter {

    var imageFromGallery: IntArray? = null
    val SHARE_IMAGE_PERMISSION_REQUEST = 10
    val SAVE_IMAGE_PERMISSION_REQUEST: Int = 10
    val generatorLoader = GeneratorLoader()
    var  modelUrl: Array<Control>? = null
    var byteArrayImage: ByteArray? = null
    override fun disconnectFaceDetector() {
        interactor.faceDetection.release()
    }

    override fun displayTitleSpinner() {
        val actions: List<String?>? = modelUrl?.toList()?.map { it.name }
        view.displayTitleSpinner(actions)

    }

    override fun readImageToBytes(imagePath: String?) {
        byteArrayImage = File(imagePath).readBytes()
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

    override fun convertToNegative1To1(progress: Int): Double {
        return ((progress - 100) / 100.00)
    }


    override fun findFacesInImage(imageWithFaces: Bitmap, context: Context) {
        try {
            val croppedFaces: MutableList<Bitmap> = interactor.getFacesFromBitmap(imageWithFaces, imageWithFaces.width, imageWithFaces.height, context)
            if (isFacesDetected(croppedFaces)) {
                view.displayFocusedImage(croppedFaces[0])
            } else {
                view.displayFocusedImage(imageWithFaces)
            }
        } catch (exception: IOException) {
            view.showError(exception.localizedMessage)
        }
    }

    fun isFacesDetected(listOfFaces: MutableList<Bitmap>): Boolean {
        return !listOfFaces.isEmpty()
    }

    override fun saveImageDisplayedToPhone(context: Context): Deferred<Boolean>? {
        var isSaved: Deferred<Boolean>? = null
        if (interactor.checkIfPermissionGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val bitmap = imageFromGallery?.let { changePixelToBitmap(it) }
            isSaved = ImageSaver().saveImageToInternalStorage(bitmap, context)
        } else {
            view.requestPermissionFromUser(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), SAVE_IMAGE_PERMISSION_REQUEST)
        }
        return isSaved
    }

    override fun transformImage(normalImage: Bitmap?, pbFile: File?) {
        normalImage?.let { findFacesInImage(it, context) }
    }

    fun loadGenerator(pbFile: File?) {
        pbFile?.let { generatorLoader.load(context.assets, it) }
    }

    override fun sampleImage(imageFromGallery: Bitmap): Deferred<IntArray> {
        return async(UI) {
            val scaled = Bitmap.createScaledBitmap(imageFromGallery, 128, 128, false)
            val encoded = generatorLoader.encode(scaled)
            return@async generatorLoader.sample(encoded)
        }
    }

    override fun changePixelToBitmap(transformedImage: IntArray): Bitmap? {
        return generatorLoader.manipulateBitmap(generatorLoader.width, generatorLoader.height, transformedImage)
    }

    override fun onRequestPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        grantResults.filter { item -> item == PackageManager.PERMISSION_GRANTED }.forEach { item ->
            if (requestCode == SHARE_IMAGE_PERMISSION_REQUEST) {
                shareImageToOtherApps()
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem, context: Context) {
        launch(UI) {
            when (item.itemId) {
                R.id.saveImage -> {
                    bg { saveImageDisplayedToPhone(context)}.await()
                    context.toast("Image Saved!")
                }
                R.id.shareIamge -> shareImageToOtherApps()
            }
        }
    }



}
