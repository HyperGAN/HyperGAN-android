package hypr.a255bits.com.hypr.Util

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream


class BitmapManipulator{

    fun compressBitmapToByteArray(compressionFormat: Bitmap.CompressFormat, image: Bitmap): ByteArray? {
        val outputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }
}