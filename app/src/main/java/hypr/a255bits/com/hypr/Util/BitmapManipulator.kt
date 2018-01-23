package hypr.a255bits.com.hypr.Util

import android.graphics.Bitmap


class BitmapManipulator{

    fun cropAreaOutOfBitmap(image: Bitmap, x: Int, y: Int, width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(image, x, y, width, height)
    }

}