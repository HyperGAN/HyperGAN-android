package hypr.a255bits.com.hypr.Main

import com.google.firebase.storage.FileDownloadTask
import hypr.a255bits.com.hypr.BuyGenerator
import hypr.a255bits.com.hypr.Generator.Control
import hypr.a255bits.com.hypr.Generator.Generator
import java.io.File

interface MainMvp {
    interface view {
        fun modeToNavBar(generator: Generator, index: Int)
        fun displayModelDownloadProgress()
        fun closeDownloadingModelDialog()
        fun startCameraActivity(indexInJson: Int)
        fun applyModelToImage(controlArray: Array<Control>, image: ByteArray?)
        fun startModelOnImage(buyGenerators: MutableList<BuyGenerator>)
        fun  displayGeneratorsOnHomePage(generators: MutableList<BuyGenerator>)

    }

    interface presenter {
        fun addModelsToNavBar()
        fun isDownloadComplete(progressPercent: Float): Boolean
        fun downloadingModelFinished()
        fun startModel(itemId: Int)
        fun startModel(itemId: Int, image: ByteArray?)
        fun createGeneratorLoader(itemId: File, itemId1: Int)
    }

    interface interactor {
        fun addModelsToNavBar(param: GeneratorListener)

        fun getModelFromFirebase(saveLocation: File, filenameInFirebase: String): FileDownloadTask
        fun showProgressOfFirebaseDownload(firebaseDownloader: FileDownloadTask)
    }

}
