package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.graphics.Bitmap
import hypr.a255bits.com.hypr.Util.ImageSaver
import java.io.IOException


class ModelFragmentPresenter(val view: ModelFragmentMVP.view, val interactor: ModelInteractor, val context: Context) : ModelFragmentMVP.presenter {

    private var  imageFromGallery: Bitmap? = null
    val SHARE_IMAGE_PERMISSION_REQUEST = 10
    override fun disconnectFaceDetector() {
        interactor.detector.release()
    }

    fun shareImageToOtherApps(){
        if(interactor.checkIfPermissionGranted(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            val shareIntent = interactor.getIntentForSharingImagesWithOtherApps(imageFromGallery)
            view.shareImageToOtherApps(shareIntent)
        }else{
            view.requestPermissionFromUser(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), SHARE_IMAGE_PERMISSION_REQUEST)
        }
    }


    override fun findFacesInImage(imageWithFaces: Bitmap, context: Context) {
        try {
            val croppedFaces: MutableList<Bitmap> = interactor.getFacesFromBitmap(imageWithFaces, imageWithFaces.width, imageWithFaces.height, context)
            if(isFacesDetected(croppedFaces)){
                view.displayFocusedImage(croppedFaces[0])
            }else{
                view.displayFocusedImage(imageWithFaces)
            }
        } catch(exception: IOException) {
            view.showError(exception.localizedMessage)
        }
    }
    fun isFacesDetected(listOfFaces: MutableList<Bitmap>): Boolean {
        return !listOfFaces.isEmpty()
    }

    override fun saveImageDisplayedToPhone() {
        val saver = ImageSaver()
        saver.saveImageToInternalStorage(imageFromGallery, context)
    }
    override fun transformImage(image: Bitmap?){
        if(image != null){
            this.imageFromGallery = image
            view.displayFocusedImage(image)
            findFacesInImage(image, context)
        }
    }
}
