package hypr.a255bits.com.hypr.MultiFaceSelection

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect

interface MultiFaceMVP{
    interface view{
        fun displayImageWithFaces(imageOfPeople: Bitmap)
        fun addBoxAroundFace(rect: Rect, canvasImageWithFaces: Canvas)

    }
    interface presenter{
        fun displayImageWithFaces(image: Bitmap?)
        fun addFaceBoxesToMultipleFacesImage(faceLocations: Context, imageOfPeoplesFaces: Bitmap?): Bitmap?
    }
}