package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri

interface ModelFragmentMVP{
    interface view{
        fun displayGallery()
        fun displayFocusedImage(imageFromGallery: Bitmap)
    }
    interface presenter{
        fun displayGallery()
        fun  getImageFromImageFileLocation(imageLocation: Uri)
        fun findFacesInImage(imageWithFaces: Bitmap, context: Context)
        fun disconnectFaceDetector()
    }
    interface interactor{
        fun  uriToBitmap(imageLocation: Uri): Bitmap
        fun  getFacesFromBitmap(imageWithFaces: Bitmap, width: Int, height: Int, context: Context): MutableList<Bitmap>
    }
}

