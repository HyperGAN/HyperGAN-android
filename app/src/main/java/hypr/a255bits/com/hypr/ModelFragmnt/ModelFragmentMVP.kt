package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri

interface ModelFragmentMVP{
    interface view{
        fun displayFocusedImage(imageFromGallery: Bitmap)
        fun  showError(errorMesssage: String)
        fun  shareImageToOtherApps(shareIntent: Intent)
    }
    interface presenter{
        fun findFacesInImage(imageWithFaces: Bitmap, context: Context)
        fun disconnectFaceDetector()
        fun saveImageDisplayedToPhone()
        fun transformImage(image: Bitmap?)
    }
    interface interactor{
        fun  getFacesFromBitmap(imageWithFaces: Bitmap, width: Int, height: Int, context: Context): MutableList<Bitmap>
        fun  getIntentForSharingImagesWithOtherApps(imageFromGallery: Bitmap?): Intent
    }
}

