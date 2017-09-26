package hypr.a255bits.com.hypr.CameraFragment

import android.net.Uri
import android.view.MenuItem


interface CameraMVP{
   interface view{
       fun  sendImageToModel(image: ByteArray?)

       fun displayGallery()
       fun navigateUpActivity()
       fun takePicture()
   }
    interface presenter{
        fun  sendPictureToModel(jpeg: ByteArray?)
        fun getImageFromImageFileLocation(imageLocation: Uri)
        fun onOptionsItemSelected(item: MenuItem?)
        fun displayGallery()
    }
    interface interactor
}