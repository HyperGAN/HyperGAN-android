package hypr.a255bits.com.hypr.CameraFragment

import android.net.Uri
import android.util.SparseArray
import com.google.android.gms.vision.face.Face


interface CameraMVP{
   interface view{
       fun  sendImageToModel(image: ByteArray?)

       fun displayGallery()
       fun navigateUpActivity()
       fun takePicture()
       fun noFaceDetectedPopup()
       fun startMultiFaceSelection(jpeg: ByteArray, facesDetected: SparseArray<Face>)
   }
    interface presenter{
        fun  sendPictureToModel(jpeg: ByteArray?)
        fun getImageFromImageFileLocation(imageLocation: Uri)
        fun displayGallery()
    }
    interface interactor
}