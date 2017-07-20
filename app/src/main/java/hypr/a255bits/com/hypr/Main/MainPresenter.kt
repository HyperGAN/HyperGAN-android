package hypr.a255bits.com.hypr.Main

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import hypr.a255bits.com.hypr.Generator

class MainPresenter(val view: MainMvp.view, val interactor: MainInteractor, val context: Context) : MainMvp.presenter {
    override fun startModel(itemId: Int) {
        val generator = interactor.listOfGenerators?.get(itemId)
        view.startModelFragment(generator?.modelUrl)
    }

    override fun addModelsToNavBar() {
        interactor.addModelsToNavBar(object: GeneratorListener {
            override fun getGenerator(generator: Generator, index: Int) {
                view.modeToNavBar(generator, index)
            }
        })
    }

    override fun findFacesInImage(imageWithFaces: Bitmap, context: Context) {
        val croppedFaces: MutableList<Bitmap> = interactor.getFacesFromBitmap(imageWithFaces, imageWithFaces.width, imageWithFaces.height, context)
        view.displayFocusedImage(croppedFaces[0])
    }

    override fun displayGallery() {
        view.displayGallery()
    }

    override fun getImageFromImageFileLocation(imageLocation: Uri) {
        val imageFromGallery: Bitmap = interactor.uriToBitmap(imageLocation)
        view.displayFocusedImage(imageFromGallery)
        findFacesInImage(imageFromGallery, context)
    }
}