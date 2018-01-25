package hypr.a255bits.com.hypr

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import hypr.a255bits.com.hypr.Util.BitmapManipulator
import hypr.a255bits.com.hypr.Util.BitmapShape


open class DrawableImageView(context: Context?, attrs: AttributeSet?) : ImageView(context, attrs) {

    var bitmap: Bitmap? = null
    val paint = Paint()
    var scaledBitmap: Bitmap? = null
    val oldFaceLocations = mutableListOf<Rect>()
    private val faceLocation = mutableListOf<Rect>()
    val touchPaint = Paint()
    private var boundsListener: DrawableImageViewTouchInBoundsListener? = null

    init {
        touchPaint.color = Color.BLUE
        touchPaint.style = Paint.Style.STROKE
        touchPaint.strokeWidth = 1.5f
    }


    fun setBoundsTouchListener(boundsListener: DrawableImageViewTouchInBoundsListener) {
        this.boundsListener = boundsListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        this.setMeasuredDimension(parentWidth, parentHeight)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        faceLocation.forEach { item ->
            oldFaceLocations.add(Rect(item))
        }
        scaledBitmap = bitmap?.let { scaleBitmap(it) }
        val centreX = (width - scaledBitmap!!.width) / 2
        val centreY = (height - scaledBitmap!!.height) / 2
        canvas?.drawBitmap(scaledBitmap, centreX.toFloat(), centreY.toFloat(), paint)
        scaleTouchInputBoxesToImagePosition(scaledBitmap!!, centreX, centreY)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        faceLocation.forEachIndexed { i, faceLocation ->
            if (faceLocation.contains(event?.x!!.toInt(), event.y.toInt())) {
                bitmap?.let { boundsListener?.onBoundsTouch(it, i) }
            }
        }

        return super.onTouchEvent(event)
    }

    private fun scaleTouchInputBoxesToImagePosition(bit: Bitmap, centreX: Int, centreY: Int) {
        faceLocation.forEach { rect ->
            val widthRatio = bit.width.toFloat() / bitmap!!.width.toFloat()
            val heightRatio = ((bit.height.toFloat())) / (bitmap!!.height.toFloat())
            rect.right = (rect.right * widthRatio).toInt()
            rect.left = (rect.left * widthRatio).toInt()
            rect.bottom = (rect.bottom * heightRatio).toInt()
            rect.bottom += centreY
            rect.top = (rect.top * heightRatio).toInt()
            rect.top += centreY
        }
    }

    private fun scaleBitmap(unscaledBitmap: Bitmap): Bitmap {
        var bm = unscaledBitmap
        var width = bm.width
        var height = bm.height

        when (BitmapManipulator().determineBitmapShape(width, height)) {
            BitmapShape.Landscape -> {
                val ratio = width.toFloat() / this.width
                width = this.width
                height = (height / ratio).toInt()
            }
            BitmapShape.Portrate -> {
                val ratio = height.toFloat() / this.height
                height = this.height
                width = (width / ratio).toInt()
            }
            BitmapShape.Square -> {
                height = this.height
                width = this.width
            }
        }
        bm = Bitmap.createScaledBitmap(bm, width, height, true)
        return bm
    }


    fun addFaceLocation(rect: Rect) {
        faceLocation.add(rect)
    }
}

interface DrawableImageViewTouchInBoundsListener {
    fun onBoundsTouch(image: Bitmap, index: Int)
}
