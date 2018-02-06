package hypr.gan.com.hypr.Util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream


class BitmapManipulator{

    fun compressBitmapToByteArray(compressionFormat: Bitmap.CompressFormat, image: Bitmap): ByteArray? {
        val outputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        return outputStream.toByteArray()
    }
    fun cropAreaOutOfBitmap(image: Bitmap, x: Int, y: Int, width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(image, x, y, width, height)
    }

    fun createBitmapFromByteArray(image: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(image, 0, image.size)

    }
}