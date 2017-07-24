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

    override fun findFacesInImage(imageWithFaces: Bitmap, context: Context) {
        try {
            val croppedFaces: MutableList<Bitmap> = interactor.getFacesFromBitmap(imageWithFaces, imageWithFaces.width, imageWithFaces.height, context)
            view.displayFocusedImage(croppedFaces[0])
        } catch(exception: IOException) {
            view.showError(exception.localizedMessage)
        }
    }

    override fun displayGallery() {
        view.displayGallery()
    }

    override fun saveImageDisplayedToPhone() {
        val saver = ImageSaver()
        saver.saveImageToInternalStorage(imageFromGallery, context)
    }
    override fun getImageFromImageFileLocation(imageLocation: Uri) {
        val imageFromGallery: Bitmap = interactor.uriToBitmap(imageLocation)
        this.imageFromGallery = imageFromGallery
        view.displayFocusedImage(imageFromGallery)
        findFacesInImage(imageFromGallery, context)
    }
}
