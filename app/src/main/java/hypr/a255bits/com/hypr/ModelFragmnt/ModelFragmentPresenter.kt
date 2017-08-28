package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.graphics.Bitmap
import hypr.a255bits.com.hypr.GeneratorLoader
import hypr.a255bits.com.hypr.Util.ImageSaver
import kotlinx.coroutines.experimental.Deferred
import java.io.File
import java.io.IOException


class ModelFragmentPresenter(val view: ModelFragmentMVP.view, val interactor: ModelInteractor, val context: Context) : ModelFragmentMVP.presenter {

    var imageFromGallery: IntArray? = null
    val SHARE_IMAGE_PERMISSION_REQUEST = 10
    val SAVE_IMAGE_PERMISSION_REQUEST: Int = 10
    override fun disconnectFaceDetector() {
        interactor.detector.release()
    }

    fun shareImageToOtherApps() {
        if (interactor.checkIfPermissionGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val bitmap = imageFromGallery?.let { view.changePixelToBitmap(it) }
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
    var saver: Deferred<Boolean>? = null
        if(interactor.checkIfPermissionGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            val bitmap = imageFromGallery?.let { view.changePixelToBitmap(it) }
            saver = ImageSaver().saveImageToInternalStorage(bitmap, context)
        }else{
            view.requestPermissionFromUser(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), SAVE_IMAGE_PERMISSION_REQUEST)
        }
    return saver
    }

    override fun transformImage(normalImage: Bitmap?, pbFile: File?, generatorLoader: GeneratorLoader) {
        if (normalImage != null) {
//            view.displayFocusedImage(normalImage)
            findFacesInImage(normalImage, context)
        }
    }

    fun loadGenerator(generatorLoader: GeneratorLoader, pbFile: File?) {
        pbFile?.let { generatorLoader.load(context.assets, it) }
    }
}
