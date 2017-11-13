package hypr.a255bits.com.hypr.CameraFragment

import android.graphics.PointF
import android.net.Uri


interface CameraMVP{
   interface view{
       fun  sendImageToModel(image: ByteArray?)

       fun displayGallery()
       fun navigateUpActivity()
       fun takePicture()
       fun noFaceDetectedPopup()
       fun startMultiFaceSelection(jpeg: ByteArray, facesDetected: MutableList<PointF>)
       fun isCameraViewEnabled(): Boolean
   }
    interface presenter{
        fun  sendPictureToModel(jpeg: ByteArray?)
        fun getImageFromImageFileLocation(imageLocation: Uri)
        fun displayGallery()
    }
    interface interactor
}