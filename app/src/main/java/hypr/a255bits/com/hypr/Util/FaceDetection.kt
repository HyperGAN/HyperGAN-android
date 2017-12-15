package hypr.a255bits.com.hypr.Util

import android.content.Context
import android.graphics.Bitmap
import android.util.SparseArray
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import hypr.a255bits.com.hypr.GeneratorLoader.FaceLocation
import hypr.a255bits.com.hypr.GeneratorLoader.GeneratorFacePosition
import hypr.a255bits.com.hypr.R
import java.io.IOException


class FaceDetection(val generatorFacePosition: GeneratorFacePosition, val context: Context) {
    var detector: FaceDetector = FaceDetector.Builder(context)
            .setTrackingEnabled(false)
            .setLandmarkType(FaceDetector.ALL_LANDMARKS)
            .build()

    fun release() {
//        detector.release()
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
        val detectFrame = if (detector.isOperational) {
            detector.detect(frame)!!
        } else {
            detector = FaceDetector.Builder(context)
                    .setTrackingEnabled(false)
                    .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                    .build()
            detector.detect(frame)
        }
        return detectFrame
    }

    fun getListOfFaces(faceLocations: SparseArray<Face>?, imageWithFaces: Bitmap, context: Context): MutableList<FaceLocation> {
        val croppedFaces = mutableListOf<FaceLocation>()
        val numOfFaces: Int = faceLocations?.size()!!
        repeat(numOfFaces) { index ->
            val faceLocation = faceLocations.valueAt(index)
            if (faceLocation != null) {
                val face = generatorFacePosition.cropFaceOutOfBitmap(faceLocation, imageWithFaces)
                face?.let { croppedFaces.add(it) }
            }
        }
        return croppedFaces
    }
}