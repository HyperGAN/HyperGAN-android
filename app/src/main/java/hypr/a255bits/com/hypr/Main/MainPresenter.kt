package hypr.a255bits.com.hypr.Main

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import hypr.a255bits.com.hypr.Generator

class MainPresenter(val view: MainMvp.view, val interactor: MainInteractor, val context: Context) : MainMvp.presenter {
    override fun startModel(itemId: Int) {
        val generator = interactor.listOfGenerators?.get(itemId)
        if (generator != null) {
            view.startModelFragment(generator.modelUrl)
        }
    }

    override fun addModelsToNavBar() {
        interactor.addModelsToNavBar(object: GeneratorListener {
            override fun getGenerator(generator: Generator, index: Int) {
                view.modeToNavBar(generator, index)
            }
        })
    }


}