package hypr.a255bits.com.hypr

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.SparseArray
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import com.google.android.gms.vision.face.Landmark


/**
 * Created by ted on 7/17/17.
 */
class MainInteractor(val context: Context) : MainMvp.interactor {
    override fun getFacesFromBitmap(imageWithFaces: Bitmap, width: Int, height: Int, context: Context) {
        val faceLocations: SparseArray<Face>? = getFaceLocationsInImage(imageWithFaces, context)

        (0..faceLocations!!.size() - 1)
                .map { faceLocations.valueAt(it) }
                .forEach {
                    for (landmark in it.landmarks) {
                        val face = cropFaceOutOfBitmap(landmark, imageWithFaces)
                    }
                }

    }


    private fun getFaceLocationsInImage(imageWithFaces: Bitmap, context: Context): SparseArray<Face>? {
        val detector = FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build()


        var faces = SparseArray<Face>()

        if (detector.isOperational) {
            println("face detection operational")

            val frame = Frame.Builder().setBitmap(imageWithFaces).build()
            faces = detector.detect(frame)
        }
        detector.release()
        return faces
    }

    private fun cropFaceOutOfBitmap(landmark: Landmark?, imageWithFaces: Bitmap) {

    }

    override fun uriToBitmap(imageLocation: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(context.contentResolver, imageLocation);
    }
}
