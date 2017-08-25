package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
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
        fromApi(23) {
            isPermissionGranted = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

        }
        return isPermissionGranted
    }

    override fun getIntentForSharingImagesWithOtherApps(imageFromGallery: Bitmap?): Intent {
        val pathToImage = MediaStore.Images.Media.insertImage(context.contentResolver, imageFromGallery, "title", null)
        val shareableIamge = Uri.parse(pathToImage)
        return createImageShareIntent(shareableIamge)
    }


    private fun writeByteArrayToFile(fileLocation: String, imageInBytes: ByteArray?) {
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


        val offsetX: Int = (0.51*256).toInt()
        val offsetY: Int = (0.4*256).toInt()
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

        // x1 < 0
        // y1 < 0
        // x2 > image.w
        // y2 > image.h

        // Create empty image X
        // Paste x1,x2 to X

        val bitmap:Bitmap =Bitmap.createBitmap(padded, x1+offsetX, y1+offsetY, w, h)
        val maxSize:Int = intArrayOf(bitmap.height.toInt(), bitmap.width.toInt()).min()!!

        val crop:Bitmap = Bitmap.createBitmap(bitmap, (bitmap.width - maxSize)/2, 0, maxSize, maxSize)

        return Bitmap.createScaledBitmap(crop, 256, 256, false)
    }

    private fun getNonNegativeValueOfFaceCoordicate(coordinate: Float): Int {
        return intArrayOf(coordinate.toInt(), 0).max()!!

    }

}
