package hypr.a255bits.com.hypr.GeneratorLoader

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.Landmark

class GeneratorFacePosition {
    private val XOFFSET_PERCENT = 0.91
    private val YOFFSET_PERCENT = 0.52
    fun cropFaceOutOfBitmap(face: Face, imageWithFaces: Bitmap): Bitmap {

        val left = face.landmarks.first { it.type == Landmark.LEFT_EYE }
        val right = face.landmarks.first { it.type == Landmark.RIGHT_EYE }


        val offsetX: Int = (XOFFSET_PERCENT * face.width).toInt()
        val offsetY: Int = (YOFFSET_PERCENT * face.height).toInt()
        val x1: Int = (left.position.x - offsetX).toInt()
        val y1: Int = (left.position.y - offsetY).toInt()
        val x2: Int = (right.position.x + offsetX).toInt()
        val y2: Int = (left.position.y + offsetY).toInt()
        val faceDimentions = Rect((left.position.x - offsetX).toInt(), (right.position.x + offsetX).toInt(),
                (left.position.y - offsetY).toInt(), (left.position.y + offsetY).toInt())

        val w: Int = faceDimentions.right - faceDimentions.left
        val h: Int = faceDimentions.bottom - faceDimentions.top
//        val w: Int = x2 - x1
//        val h: Int = y2 - y1


        val bitmapWithPadding: Bitmap = placePaddingAroundBitmap(imageWithFaces, offsetX, offsetY)

        val bitmap: Bitmap = Bitmap.createBitmap(bitmapWithPadding, x1 + offsetX, y1 + offsetY, w, h)
        val maxSize: Int = intArrayOf(bitmap.height, bitmap.width).min()!!

        return getResizedBitmap(bitmap, maxSize, maxSize)
    }

    private fun placePaddingAroundBitmap(imageWithFaces: Bitmap, offsetX: Int, offsetY: Int): Bitmap {
        val hpad = imageWithFaces.height + offsetY * 2
        val wpad = imageWithFaces.width + offsetX * 2

        val padded: Bitmap = Bitmap.createBitmap(wpad, hpad, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(padded)
        canvas.drawBitmap(imageWithFaces, offsetX.toFloat(), offsetY.toFloat(), null)
        return padded
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