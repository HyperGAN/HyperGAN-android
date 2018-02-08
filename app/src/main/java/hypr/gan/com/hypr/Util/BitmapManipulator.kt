package hypr.gan.com.hypr.Util

import android.graphics.Bitmap


class BitmapManipulator{
    fun determineBitmapShape(width: Int, height: Int): BitmapShape {
        var shape = BitmapShape.Square
        if(width > height){
           shape = BitmapShape.Landscape
        }else if( height > width){shape = BitmapShape.Portrate}
        return shape
    }

    fun cropAreaOutOfBitmap(image: Bitmap, x: Int, y: Int, width: Int, height: Int): Bitmap {
        return Bitmap.createBitmap(image, x, y, width, height)
    }

}
enum class BitmapShape{
    Square, Landscape, Portrate
}