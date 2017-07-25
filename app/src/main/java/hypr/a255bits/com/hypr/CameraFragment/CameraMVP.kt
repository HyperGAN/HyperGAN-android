package hypr.a255bits.com.hypr.CameraFragment

import android.graphics.Bitmap


interface CameraMVP{
   interface view{
       fun  sendImageToModel(image: ByteArray?)

   }
    interface presenter{
        fun  sendPictureToModel(jpeg: ByteArray?)

    }
}