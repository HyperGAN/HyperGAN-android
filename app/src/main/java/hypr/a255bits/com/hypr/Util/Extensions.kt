package hypr.a255bits.com.hypr.Util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.view.ViewPager
import com.flurgle.camerakit.CameraListener
import com.flurgle.camerakit.CameraView
import java.io.ByteArrayOutputStream


fun Float.nonNegativeInt(): Int {
    return intArrayOf(this.toInt(), 0).max()!!
}

fun Int.negative1To1(): Double {
    return ((this - 100) / 100.00)
}

inline fun ViewPager.onPageSelected(crossinline listener: (position: Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
            listener(position)
        }

    })
}
inline fun CameraView.onPictureTaken(crossinline listener: (jpeg: ByteArray?) -> Unit){
   setCameraListener(object: CameraListener(){
       override fun onPictureTaken(jpeg: ByteArray?) {
           super.onPictureTaken(jpeg)
           listener(jpeg)
       }
   })
}

fun ByteArray.toBitmap(): Bitmap? {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}

fun Bitmap.toByteArray(): ByteArray{
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
