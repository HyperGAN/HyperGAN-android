package hypr.a255bits.com.hypr

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri

/**
 * Created by ted on 7/17/17.
 */

class MainPresenter(val applicationContext: Context, val view: MainMvp.view, val interactor: MainInteractor) : MainMvp.presenter{
    override fun findFacesInImage(imageWithFaces: Bitmap, context: Context) {
        interactor.getFacesFromBitmap(imageWithFaces, imageWithFaces.width, imageWithFaces.height, context)
    }

    override fun displayGallery() {
        view.displayGallery()
    }

    override fun getImageFromImageFileLocation(imageLocation: Uri) {
        val imageFromGallery: Bitmap = interactor.uriToBitmap(imageLocation)
        view.displayFocusedImage(imageFromGallery)
    }
}