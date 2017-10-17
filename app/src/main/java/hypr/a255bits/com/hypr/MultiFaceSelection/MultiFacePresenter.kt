package hypr.a255bits.com.hypr.MultiFaceSelection

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import collections.forEach
import com.google.android.gms.vision.face.Face
import hypr.a255bits.com.hypr.Util.FaceDetection

class MultiFacePresenter(val view: MultiFaceMVP.view) : MultiFaceMVP.presenter {


    override fun displayImageWithFaces(imageOfPeoplesFaces: Bitmap?) {
        imageOfPeoplesFaces?.let { view.displayImageWithFaces(it) }
    }

    override fun addFaceBoxesToMultipleFacesImage(context: Context, imageOfPeoplesFaces: Bitmap?): Bitmap? {
        var face: Bitmap? = null
        if (imageOfPeoplesFaces != null) {
            val faceLocations = FaceDetection(context).getFaceLocations(imageOfPeoplesFaces, context)
            face = imageOfPeoplesFaces.copy(Bitmap.Config.ARGB_8888, true)
            val canvasImageWithFaces = Canvas(face)
            faceLocations?.forEach { i, face ->
                val rect = getFaceBoxLocationInImage(face)
                view.addBoxAroundFace(rect, canvasImageWithFaces)
            }
        }
        return face
    }

    private fun getFaceBoxLocationInImage(face: Face): Rect {
        return with(face) {
            val left: Int = (position.x - width).toInt()
            val right: Int = (position.x + height).toInt()
            val top: Int = (position.y - height).toInt()
            val bottom: Int = (position.y + height).toInt()
            Rect(left, top, right, bottom)
        }
    }

}