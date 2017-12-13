package hypr.a255bits.com.hypr.Util

import android.graphics.Rect

/**
 * Created by ted on 12/11/17.
 */

class FaceCropper {

    fun faceToRect(x: Float, y: Float, width: Float, height: Float): Rect {
        val left: Int = (x - (width / 2)).toInt()
        val right: Int = (x + (width / 2)).toInt()
        val top: Int = (y - (height / 2)).toInt()
        val bottom: Int = (y + (height / 2)).toInt()
        return Rect(left, top, right, bottom)
    }
}