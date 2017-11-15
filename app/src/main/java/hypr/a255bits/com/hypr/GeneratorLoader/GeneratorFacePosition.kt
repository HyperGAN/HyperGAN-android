package hypr.a255bits.com.hypr.GeneratorLoader

import android.content.Context
import android.graphics.*
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.Landmark
import hypr.a255bits.com.hypr.Util.FaceDetection

class GeneratorFacePosition(val context: Context) {
    private val XOFFSET_PERCENT = 0.85
    private val YOFFSET_PERCENT = 0.54
    fun cropFaceOutOfBitmap(face: Face, imageWithFaces: Bitmap): FaceLocation {

        val left = face.landmarks.first{ it.type == Landmark.LEFT_EYE }
        val right = face.landmarks.first{ it.type == Landmark.RIGHT_EYE }


        val offsetX: Int = (XOFFSET_PERCENT *face.width).toInt()
        val offsetY: Int = (YOFFSET_PERCENT*face.height).toInt()
        val x1: Int = (left.position.x - offsetX).toInt()
        val y1: Int = (left.position.y - offsetY).toInt()
        val x2: Int = (right.position.x + offsetX).toInt()
        val y2: Int = (left.position.y + offsetY).toInt()

        val rect = FaceDetection(context).faceToRect(face.position.x, face.position.y, face.width, face.height)
        rect.left = rect.left - offsetX
        rect.right = rect.right + offsetX
        rect.top = rect.top - offsetY
        rect.bottom = rect.bottom + offsetY
        val mutableBitmap = imageWithFaces.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        canvas.drawRect(rect, paint)

        var w: Int = x2-x1
        var h: Int = y2-y1


        var bitmap: Bitmap
        try{
            bitmap = Bitmap.createBitmap(imageWithFaces, x1, y1, w, h)
        }catch(e: IllegalArgumentException){
            bitmap = Bitmap.createBitmap(imageWithFaces, face.position.x.toInt(), face.position.y.toInt(), face.width.toInt(), face.height.toInt())
        }
        val scaledRect = Rect(x1, y1, x1 + w,y1 + h)
        val maxSize:Int = intArrayOf(bitmap.height, bitmap.width).min()!!
        val resizedBitmap = getResizedBitmap(bitmap, maxSize, maxSize)
        return FaceLocation(resizedBitmap, scaledRect)
    }

    fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        val resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false)
        bm.recycle()
        return resizedBitmap
    }
}