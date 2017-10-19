package hypr.a255bits.com.hypr.MultiFaceSelection

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import java.io.File

interface MultiFaceMVP{
    interface view{
        fun displayImageWithFaces(imageOfPeople: Bitmap)
        fun addBoxAroundFace(rect: Rect, canvasImageWithFaces: Canvas)
        fun addFaceLocationToImage(rect: Rect)

        fun sendImageToModel(file: File)
    }
    interface presenter{
        fun displayImageWithFaces(image: Bitmap?)
        fun addFaceBoxesToMultipleFacesImage(faceLocations: Context, imageOfPeoplesFaces: Bitmap?): Bitmap?
        fun cropFaceFromImage(image: Bitmap, bounds: Int): Bitmap
        fun sendCroppedFaceToMultiModel(croppedFace: Bitmap)
        fun saveImageSoOtherFragmentCanViewIt(image: ByteArray?): File
    }
}