package hypr.a255bits.com.hypr.CameraFragment

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream


class CameraInteractor(val context: Context): CameraMVP.interactor{

    override fun uriToBitmap(imageLocation: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(context.contentResolver, imageLocation)
    }
    fun uriToByteArray(imageLocation: Uri): ByteArray? {
        val bitmap = uriToBitmap(imageLocation)
        return bitmapToByteArray(bitmap)
    }
    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()

    }
}