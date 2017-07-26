package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.SparseArray
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector

import hypr.a255bits.com.hypr.R
import hypr.a255bits.com.hypr.Util.BitmapManipulator
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ModelInteractor(val context: Context) : ModelFragmentMVP.interactor {
    val detector: FaceDetector by lazy {
        FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build()
    }

    override fun getIntentForSharingImagesWithOtherApps(imageFromGallery: Bitmap?): Intent {
        val imageInBytes = imageFromGallery?.let { BitmapManipulator().compressBitmapToByteArray(Bitmap.CompressFormat.PNG, it) }

        val tempStorageFileLocation = Environment.getExternalStorageDirectory().absolutePath + File.separator + "temporary_file.jpg"
        writeByteArrayToFile(tempStorageFileLocation, imageInBytes)

        return createImageShareIntent(Uri.parse("file:///sdcard/temporary_file.jpg"))
    }


    private fun writeByteArrayToFile(fileLocation: String, imageInBytes: ByteArray?){
        val file = File(fileLocation)
        file.createNewFile()
        val fileOutput = FileOutputStream(file)
        fileOutput.write(imageInBytes)
    }

    private fun createImageShareIntent(parse: Uri?): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/jpeg"
        shareIntent.putExtra(Intent.EXTRA_STREAM, parse)
        return shareIntent
    }

    @Throws(IOException::class)
    override fun getFacesFromBitmap(imageWithFaces: Bitmap, width: Int, height: Int, context: Context): MutableList<Bitmap> {
        val faceLocations: SparseArray<Face>? = getFaceLocations(imageWithFaces, context)
        return getListOfFaces(faceLocations, imageWithFaces)
    }

    @Throws(IOException::class)
    private fun getFaceLocations(imageWithFaces: Bitmap, context: Context): SparseArray<Face>? {
        var locationOfFaces = SparseArray<Face>()
        if (detector.isOperational) {
            val frame = Frame.Builder().setBitmap(imageWithFaces).build()
            locationOfFaces = detector.detect(frame)
        } else {
            throw IOException(context.resources.getString(R.string.failed_face_detection))
        }
        return locationOfFaces
    }

    private fun getListOfFaces(faceLocations: SparseArray<Face>?, imageWithFaces: Bitmap): MutableList<Bitmap> {
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
        val x: Int = getNonNegativeValueOfFaceCoordicate(centerOfFace.x)
        val y: Int = getNonNegativeValueOfFaceCoordicate(centerOfFace.y)

        return Bitmap.createBitmap(imageWithFaces, x, y, face.width.toInt(), face.height.toInt())
    }

    private fun getNonNegativeValueOfFaceCoordicate(coordinate: Float): Int {
        return intArrayOf(coordinate.toInt(), 0).max()!!

    }

    override fun uriToBitmap(imageLocation: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(context.contentResolver, imageLocation)
    }
}
