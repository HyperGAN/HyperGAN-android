package hypr.a255bits.com.hypr.Util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.SparseArray
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import hypr.a255bits.com.hypr.GeneratorLoader.FaceLocation
import hypr.a255bits.com.hypr.GeneratorLoader.GeneratorFacePosition
import hypr.a255bits.com.hypr.R
import java.io.IOException


class FaceDetection(val context: Context) {
    var detector: FaceDetector =
            FaceDetector.Builder(context)
                    .setTrackingEnabled(false)
                    .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                    .build()

    fun release() {
        detector.release()
    }

    @Throws(IOException::class)
    fun getFaceLocations(imageWithFaces: Bitmap, context: Context): SparseArray<Face>? {
        var locationOfFaces = SparseArray<Face>()
        if (detector.isOperational) {
            detector =
                    FaceDetector.Builder(context)
                            .setTrackingEnabled(false)
                            .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                            .build()

            locationOfFaces = getLocationOfFaces(imageWithFaces)
            release()
        } else {
            throw IOException(context.resources.getString(R.string.failed_face_detection))
        }
        return locationOfFaces
    }

    private fun getLocationOfFaces(imageWithFaces: Bitmap): SparseArray<Face> {
        val frame = Frame.Builder().setBitmap(imageWithFaces).build()
        return detector.detect(frame)
    }

    fun getListOfFaces(faceLocations: SparseArray<Face>?, imageWithFaces: Bitmap): MutableList<FaceLocation> {
        val croppedFaces = mutableListOf<FaceLocation>()
        val numOfFaces: Int = faceLocations?.size()!!
        repeat(numOfFaces) { index ->
            val faceLocation = faceLocations.valueAt(index)
            if (faceLocation != null) {
                val face = GeneratorFacePosition(context).cropFaceOutOfBitmap(faceLocation, imageWithFaces)
                face?.let { croppedFaces.add(it) }
            }
        }
        return croppedFaces
    }

    private fun cropFaceOutOfBitmap(face: Face, imageWithFaces: Bitmap): Bitmap {
        val centerOfFace = face.position
        val x = centerOfFace.x.nonNegativeInt()
        val y = centerOfFace.y.nonNegativeInt()
        return BitmapManipulator().cropAreaOutOfBitmap(imageWithFaces, x, y, face.width.toInt(), face.height.toInt())
    }

    fun faceToRect(x: Float, y: Float, width: Float, height: Float): Rect {
        val left: Int = (x - (width / 2)).toInt()
        val right: Int = (x + (width / 2)).toInt()
        val top: Int = (y - (height / 2)).toInt()
        val bottom: Int = (y + (height / 2)).toInt()
        return Rect(left, top, right, bottom)
    }
}