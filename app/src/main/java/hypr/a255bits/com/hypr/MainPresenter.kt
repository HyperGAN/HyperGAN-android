package hypr.a255bits.com.hypr

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri

class MainPresenter(val applicationContext: Context, val view: MainMvp.view, val interactor: MainInteractor) : MainMvp.presenter{

    override fun displayGallery() {
        view.displayGallery()
    }

    override fun getImageFromImageFileLocation(imageLocation: Uri) {
        val imageFromGallery: Bitmap = interactor.uriToBitmap(imageLocation)
        view.displayFocusedImage(imageFromGallery)
    }
}