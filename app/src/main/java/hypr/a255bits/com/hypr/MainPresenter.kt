package hypr.a255bits.com.hypr

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import hypr.a255bits.com.hypr.Util.ImageSaver

class MainPresenter(val view: MainMvp.view, val interactor: MainInteractor, val context: Context) : MainMvp.presenter{
    override fun saveImageToInternalStorage(focusedImageBitmap: Bitmap?) {
        val isSavedToInternalStorage = ImageSaver().saveImageToInternalStorage(focusedImageBitmap, context)
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