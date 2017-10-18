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
            faceLocations?.forEach { i, facrCoordinate ->
                val rect = getFaceBoxLocationInImage(facrCoordinate)
                println("faceLocation: ${facrCoordinate.position.y}:${facrCoordinate.position.x}")
                println("rect: ${rect.centerY()}:${rect.centerX()}")
                view.addBoxAroundFace(rect, canvasImageWithFaces)
                view.addFaceLocationToImage(rect)
            }
        }
        return face
    }

    private fun getFaceBoxLocationInImage(face: Face): Rect {
        return with(face) {
//            val left: Int = (position.x  - (width / 2)).toInt()
//            val right: Int = (position.x - (height / 2)).toInt()
//            val top: Int = (position.y - (height / 2)).toInt()
//            val bottom: Int = (position.y - (height / 2)).toInt()

            val left: Int = (position.x  - (0)).toInt()
            val right: Int = (position.x + (width)).toInt()
            val top: Int = (position.y - (0)).toInt()
            val bottom: Int = (position.y + (height )).toInt()
            Rect(left, top, right, bottom)
        }
    }

}