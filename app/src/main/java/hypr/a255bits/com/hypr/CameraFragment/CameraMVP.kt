package hypr.a255bits.com.hypr.CameraFragment

import android.graphics.Bitmap
import android.net.Uri


interface CameraMVP{
   interface view{
       fun  sendImageToModel(image: ByteArray?)

       fun displayGallery()
   }
    interface presenter{
        fun  sendPictureToModel(jpeg: ByteArray?)
        fun getImageFromImageFileLocation(imageLocation: Uri)
    }
    interface interactor{
        fun  uriToBitmap(imageLocation: Uri): Bitmap

    }
}