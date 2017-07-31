package hypr.a255bits.com.hypr.Main

import android.content.Context
import hypr.a255bits.com.hypr.Generator

class MainPresenter(val view: MainMvp.view, val interactor: MainInteractor, val context: Context) : MainMvp.presenter {

    private val  DOWNLOAD_COMPLETE: Float = 100.0f
    override fun startModel(itemId: Int) {
        val generator = interactor.listOfGenerators?.get(itemId)
        if (generator != null) {
//            view.displayModelDownloadProgress()
//            val file = File.createTempFile("optimized_weight_conv", "pb")
//            val filePointer = interactor.getModelFromFirebase(file, "optimized_weight_conv.pb")
//            interactor.showProgressOfFirebaseDownload(filePointer)
            view.startModelFragment(itemId)
        }
    }
    override fun startModel(itemId: Int, image: ByteArray?) {
        val generator = interactor.listOfGenerators?.get(itemId)
        if (generator != null) {
            view.applyModelToImage(generator.modelUrl, image)
        }
    }
    override fun downloadingModelFinished() {
        view.closeDownloadingModelDialog()
    }

    override fun isDownloadComplete(progressPercent: Float): Boolean {
        return progressPercent >= DOWNLOAD_COMPLETE
    }

    override fun addModelsToNavBar() {
        interactor.addModelsToNavBar(object : GeneratorListener {
            override fun getGenerator(generator: Generator, index: Int) {
                view.modeToNavBar(generator, index)
                view.startModelOnImage()
            }
        })
    }


}