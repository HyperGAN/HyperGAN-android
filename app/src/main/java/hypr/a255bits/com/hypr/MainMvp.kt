package hypr.a255bits.com.hypr

import android.graphics.Bitmap
import android.net.Uri

/**
 * Created by ted on 7/17/17.
 */
interface MainMvp {
    interface view{
        fun displayGallery()
        fun displayFocusedImage(imageFromGallery: Bitmap)

    }
    interface presenter{

        fun displayGallery()
        fun  getImageFromImageFileLocation(imageLocation: Uri)
    }

    interface interactor {
        fun  uriToBitmap(imageLocation: Uri): Any

    }

}
