package hypr.a255bits.com.hypr

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import org.jetbrains.anko.toast


class DrawableImageView(context: Context?, attrs: AttributeSet?) : ImageView(context, attrs) {
    var bitmap: Bitmap? = null
    val paint = Paint()
    private val faceLocation = mutableListOf<Rect>()
    val touchPaint = Paint()

    init{
        touchPaint.color = Color.BLUE
        touchPaint.style = Paint.Style.STROKE
        touchPaint.strokeWidth = 1.5f
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        this.setMeasuredDimension(parentWidth, parentHeight)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val bit = scaleBitmap(bitmap!!)
        val centreX = (width - bit.width) / 2
        val centreY = (height - bit.height) / 2
        canvas?.drawBitmap(bit, centreX.toFloat(), centreY.toFloat(), paint)
        scaleTouchInputBoxesToImagePosition(bit, centreX, centreY)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        faceLocation.forEach { faceLocation ->
            if(faceLocation.contains(event?.x!!.toInt(), event.y.toInt())){
               context.toast("inside bounds")
            }
        }

        return super.onTouchEvent(event)
    }

    private fun scaleTouchInputBoxesToImagePosition(bit: Bitmap, centreX: Int, centreY: Int){
        faceLocation.forEach { rect ->
            val widthDifference = bit.width.toFloat() / bitmap!!.width.toFloat()
            val heightDifference = ((bit.height.toFloat()))/ (bitmap!!.height.toFloat())
            rect.right = (rect.right * widthDifference).toInt()
            rect.left = (rect.left * widthDifference).toInt()
            rect.bottom = (rect.bottom * heightDifference).toInt()
            rect.bottom += centreY
            rect.top = (rect.top * heightDifference).toInt()
            rect.top += centreY
        }
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

    fun addFaceLocation(rect: Rect) {
        faceLocation.add(rect)
    }
}
