package hypr.a255bits.com.hypr.Util

import android.content.Context
import android.graphics.Bitmap
import android.util.SparseArray
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import hypr.a255bits.com.hypr.R
import java.io.IOException


class FaceDetection(val context: Context){
    val detector: FaceDetector by lazy {
        FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build()
    }
    fun release(){
        detector.release()
    }
    @Throws(IOException::class)
    fun getFaceLocations(imageWithFaces: Bitmap, context: Context): SparseArray<Face>? {
        var locationOfFaces = SparseArray<Face>()
        if (detector.isOperational) {
            locationOfFaces = getLocationOfFaces(imageWithFaces)
        } else {
            throw IOException(context.resources.getString(R.string.failed_face_detection))
        }
        return locationOfFaces
    }
    private fun getLocationOfFaces(imageWithFaces: Bitmap): SparseArray<Face> {
        val frame = Frame.Builder().setBitmap(imageWithFaces).build()
        return detector.detect(frame)
    }
    fun getListOfFaces(faceLocations: SparseArray<Face>?, imageWithFaces: Bitmap): MutableList<Bitmap> {
        val croppedFaces = mutableListOf<Bitmap>()
        val numOfFaces: Int = faceLocations?.size()!!
        repeat(numOfFaces) { index ->
            val faceLocation = faceLocations.valueAt(index)
            if (faceLocation != null) {
                val face = cropFaceOutOfBitmap(faceLocation, imageWithFaces)
                croppedFaces.add(face)
            }
        }
        return croppedFaces
    }
    private fun cropFaceOutOfBitmap(face: Face, imageWithFaces: Bitmap): Bitmap {
        val centerOfFace = face.position
        val x = centerOfFace.x.nonNegativeInt()
        val y = centerOfFace.y.nonNegativeInt()
        return BitmapManipulator().cropAreaOutOfBitmap(imageWithFaces,x,y,face.width.toInt(), face.height.toInt())
    }
}