package hypr.a255bits.com.hypr

import android.net.Uri

/**
 * Created by ted on 7/17/17.
 */
interface MainMvp {
    interface view{
        fun displayGallery()
        fun displayFocusedImage()

    }
    interface presenter{

        fun displayGallery()
        fun  getImageFromImageFileLocation(imageLocation: Uri)
    }

    interface interactor {
        fun  uriToBitmap(): Any

    }

}
