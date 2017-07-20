package hypr.a255bits.com.hypr.Main

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import hypr.a255bits.com.hypr.Generator

interface MainMvp {
    interface view{
        fun displayGallery()
        fun displayFocusedImage(imageFromGallery: Bitmap)
        fun  modeToNavBar(generator: Generator, index: Int)
        fun  startModelFragment(modelUrl: String?)

    }
    interface presenter{

        fun displayGallery()
        fun  getImageFromImageFileLocation(imageLocation: Uri)
        fun findFacesInImage(imageWithFaces: Bitmap, context: Context)
        fun addModelsToNavBar()
        fun  startModel(itemId: Int)
    }

    interface interactor {
        fun  uriToBitmap(imageLocation: Uri): Bitmap
        fun  getFacesFromBitmap(imageWithFaces: Bitmap, width: Int, height: Int, context: Context): MutableList<Bitmap>
        fun addModelsToNavBar(param: GeneratorListener)

    }

}
