package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.SparseArray
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import com.google.android.gms.vision.face.Landmark
import com.pawegio.kandroid.fromApi

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

    override fun checkIfPermissionGranted(permission: String): Boolean {
        var isPermissionGranted = true
        fromApi(23){
            isPermissionGranted = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

        }
        return isPermissionGranted
    }
    override fun getIntentForSharingImagesWithOtherApps(imageFromGallery: Bitmap?): Intent {
        val pathToImage = MediaStore.Images.Media.insertImage(context.contentResolver, imageFromGallery, "title", null)
        val shareableIamge = Uri.parse(pathToImage)
        return createImageShareIntent(shareableIamge)
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
        val landmarks = face.landmarks
        val left = face.landmarks.first{ it.type == Landmark.LEFT_EYE }
        val right = face.landmarks.first{ it.type == Landmark.RIGHT_EYE }


        val offsetX = 200
        val offsetY = 200
        val x: Int = getNonNegativeValueOfFaceCoordicate(left.position.x - offsetX)
        val y: Int = getNonNegativeValueOfFaceCoordicate(left.position.y - offsetY)
        Log.d("left-right", "l " + left.position.toString() + " r " + right.position.toString())
        var w: Int = face.width.toInt() + offsetX
        var h: Int = face.height.toInt() + offsetY

        if(x+w > imageWithFaces.width) {
            w = imageWithFaces.width-x
        }
        if(y+h > imageWithFaces.height) {
            h = imageWithFaces.height-y
        }

        val bitmap:Bitmap =Bitmap.createBitmap(imageWithFaces, x, y, w, h)
        val maxSize:Int = intArrayOf(bitmap.height.toInt(), bitmap.width.toInt()).min()!!

        val crop:Bitmap = Bitmap.createBitmap(bitmap, (bitmap.width - maxSize)/2, 0, maxSize, maxSize)

        return Bitmap.createScaledBitmap(crop, 256, 256, false)
    }

    private fun getNonNegativeValueOfFaceCoordicate(coordinate: Float): Int {
        return intArrayOf(coordinate.toInt(), 0).max()!!

    }

}
