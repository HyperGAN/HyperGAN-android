package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import hypr.a255bits.com.hypr.Util.ImageSaver
import java.io.IOException


class ModelFragmentPresenter(val view: ModelFragmentMVP.view, val interactor: ModelInteractor, val context: Context) : ModelFragmentMVP.presenter {

    private var  imageFromGallery: Bitmap? = null
    override fun disconnectFaceDetector() {
        interactor.detector.release()
    }

    fun shareImageToOtherApps(){
       val shareIntent = interactor.getIntentForSharingImagesWithOtherApps(imageFromGallery)
        view.shareImageToOtherApps(shareIntent)
    }
    override fun findFacesInImage(imageWithFaces: Bitmap, context: Context) {
        try {
            val croppedFaces: MutableList<Bitmap> = interactor.getFacesFromBitmap(imageWithFaces, imageWithFaces.width, imageWithFaces.height, context)
            if(!croppedFaces.isEmpty()){
                view.displayFocusedImage(croppedFaces[0])
            }else{
                view.displayFocusedImage(imageWithFaces)
            }
        } catch(exception: IOException) {
            view.showError(exception.localizedMessage)
        }
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
