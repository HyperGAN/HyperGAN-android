package hypr.a255bits.com.hypr.GeneratorLoader

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.Landmark

/**
 * Created by tedho on 8/25/2017.
 */
class GeneratorFacePosition{
    fun cropFaceOutOfBitmap(face: Face, imageWithFaces: Bitmap): Bitmap {

        val centerOfFace = face.position
        val landmarks = face.landmarks
        val left = face.landmarks.first{ it.type == Landmark.LEFT_EYE }
        val right = face.landmarks.first{ it.type == Landmark.RIGHT_EYE }


        val offsetX: Int = (0.91*face.width).toInt()
        val offsetY: Int = (0.52*face.height).toInt()
        val x1: Int = (left.position.x - offsetX).toInt()
        val y1: Int = (left.position.y - offsetY).toInt()
        val x2: Int = (right.position.x + offsetX).toInt()
        val y2: Int = (left.position.y + offsetY).toInt()

        var w: Int = x2-x1
        var h: Int = y2-y1

        val hpad = imageWithFaces.height + offsetY*2
        val wpad = imageWithFaces.width + offsetX*2

        val padded:Bitmap =Bitmap.createBitmap(wpad, hpad, Bitmap.Config.ARGB_8888)
        val canvas:Canvas = Canvas(padded)
        canvas.drawBitmap(imageWithFaces, offsetX.toFloat(), offsetY.toFloat(), null)

        val bitmap:Bitmap =Bitmap.createBitmap(padded, x1+offsetX, y1+offsetY, w, h)
        val maxSize:Int = intArrayOf(bitmap.height, bitmap.width).min()!!
        padded.recycle()

        val scaledBitmap = getResizedBitmap(bitmap, maxSize, maxSize)
        return scaledBitmap
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