package hypr.gan.com.hypr.CameraFragment

import android.graphics.Bitmap
import android.graphics.PointF
import android.net.Uri


interface CameraMVP{
   interface view{
       fun  sendImageToModel(image: ByteArray?, croppedFace: Bitmap)

       fun displayGallery()
       fun navigateUpActivity()
       fun takePicture()
       fun noFaceDetectedPopup()
       fun startMultiFaceSelection(jpeg: ByteArray, facesDetected: MutableList<PointF>)
       fun isCameraViewEnabled(): Boolean
       fun showFaceTooCloseErrorTryAgain()
   }
    interface presenter{
        fun  sendPictureToModel(jpeg: ByteArray?)
        fun getImageFromImageFileLocation(imageLocation: Uri)
        fun displayGallery()
    }
}