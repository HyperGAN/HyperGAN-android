package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri


class ModelFragmentPresenter(val view: ModelFragmentMVP.view, val interactor: ModelInteractor, val context: Context): ModelFragmentMVP.presenter{
    override fun disconnectFaceDetector() {
        interactor.detector.release()
    }

    override fun findFacesInImage(imageWithFaces: Bitmap, context: Context) {
        val croppedFaces: MutableList<Bitmap> = interactor.getFacesFromBitmap(imageWithFaces, imageWithFaces.width, imageWithFaces.height, context)
        view.displayFocusedImage(croppedFaces[0])
    }

    override fun displayGallery() {
        view.displayGallery()
    }

    override fun getImageFromImageFileLocation(imageLocation: Uri) {
        val imageFromGallery: Bitmap = interactor.uriToBitmap(imageLocation)
        view.displayFocusedImage(imageFromGallery)
        findFacesInImage(imageFromGallery, context)
    }
}
