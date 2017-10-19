package hypr.a255bits.com.hypr.MultiFaceSelection

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.util.SparseArray
import collections.forEach
import com.google.android.gms.vision.face.Face
import hypr.a255bits.com.hypr.Util.BitmapManipulator
import hypr.a255bits.com.hypr.Util.FaceDetection
import hypr.a255bits.com.hypr.Util.ImageSaver
import hypr.a255bits.com.hypr.Util.toByteArray
import java.io.File

class MultiFacePresenter(val view: MultiFaceMVP.view) : MultiFaceMVP.presenter {
    var imageOfPeoplesFaces: Bitmap? = null
    lateinit var faceCoordinates: SparseArray<Face>


    override fun displayImageWithFaces(imageOfPeoplesFaces: Bitmap?) {
        imageOfPeoplesFaces?.let { view.displayImageWithFaces(it) }
    }

    override fun addFaceBoxesToMultipleFacesImage(context: Context, imageOfPeoplesFaces: Bitmap?): Bitmap? {
        var face: Bitmap? = null
        this.imageOfPeoplesFaces = imageOfPeoplesFaces
        if (imageOfPeoplesFaces != null) {
            val faceLocations = FaceDetection(context).getFaceLocations(imageOfPeoplesFaces, context)
            this.faceCoordinates = faceLocations!!
            face = imageOfPeoplesFaces.copy(Bitmap.Config.ARGB_8888, true)
            val canvasImageWithFaces = Canvas(face)
            faceLocations?.forEach { i, facrCoordinate ->
                val rect = getFaceBoxLocationInImage(facrCoordinate)
                view.addBoxAroundFace(rect, canvasImageWithFaces)
                view.addFaceLocationToImage(rect)
            }
        }
        return face
    }

    override fun sendCroppedFaceToMultiModel(croppedFace: Bitmap) {
        val file = saveImageSoOtherFragmentCanViewIt(croppedFace.toByteArray())
        view.sendImageToModel(file)
    }

    override fun saveImageSoOtherFragmentCanViewIt(image: ByteArray?): File {
        val file = File.createTempFile("image", "png")
        ImageSaver().saveImageToFile(file, image)
        return file
    }

    private fun getFaceBoxLocationInImage(face: Face): Rect {
        return with(face) {
            //            val left: Int = (position.x  - (width / 2)).toInt()
//            val right: Int = (position.x - (height / 2)).toInt()
//            val top: Int = (position.y - (height / 2)).toInt()
//            val bottom: Int = (position.y - (height / 2)).toInt()

            val left: Int = (position.x - (0)).toInt()
            val right: Int = (position.x + (width)).toInt()
            val top: Int = (position.y - (0)).toInt()
            val bottom: Int = (position.y + (height)).toInt()
            Rect(left, top, right, bottom)
        }
    }

    override fun cropFaceFromImage(image: Bitmap, index: Int): Bitmap {
        val key = faceCoordinates.keyAt(index)
        with(faceCoordinates[key]){
            return BitmapManipulator().cropAreaOutOfBitmap(image, position.x.toInt(), position.y.toInt(), width.toInt(), height.toInt())
        }
    }

}