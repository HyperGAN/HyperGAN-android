package hypr.a255bits.com.hypr.Util

import android.support.v4.view.ViewPager
import com.flurgle.camerakit.CameraListener
import com.flurgle.camerakit.CameraView

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
