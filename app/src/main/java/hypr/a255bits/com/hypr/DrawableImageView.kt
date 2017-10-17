package hypr.a255bits.com.hypr

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ImageView



class DrawableImageView(context: Context?, attrs: AttributeSet?) : ImageView(context, attrs) {
    var bitmap: Bitmap? = null
    val paint = Paint()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        this.setMeasuredDimension(parentWidth, parentHeight)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val bit = scaleBitmap(bitmap!!)
        val centreX = (width  - bit.width) /2
        val centreY = (height - bit.height) /2
        canvas?.drawBitmap(bit, centreX.toFloat(), centreY.toFloat(), paint)
    }

    private fun scaleBitmap(bm: Bitmap): Bitmap {
        var bm = bm
        var width = bm.width
        var height = bm.height

        if (width > height) {
            // landscape
            val ratio = width.toFloat() / this.width
            width = this.width
            height = (height / ratio).toInt()
        } else if (height > width) {
            // portrait
            val ratio = height.toFloat() / this.height
            height = this.height
            width = (width / ratio).toInt()
        } else {
            // square
            height = this.height
            width = this.width
        }

        bm = Bitmap.createScaledBitmap(bm, width, height, true)
        return bm
    }
}
