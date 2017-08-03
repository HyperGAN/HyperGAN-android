package hypr.a255bits.com.hypr.Main

import hypr.a255bits.com.hypr.Generator

import com.google.firebase.storage.FileDownloadTask
import java.io.File

interface MainMvp {
    interface view {
        fun modeToNavBar(generator: Generator, index: Int)
        fun displayModelDownloadProgress()
        fun closeDownloadingModelDialog()
        fun startModelFragment(indexInJson: Int)
        fun applyModelToImage(modelUrl: String, image: ByteArray?)
        fun startModelOnImage()

    }

    interface presenter {
        fun addModelsToNavBar()
        fun isDownloadComplete(progressPercent: Float): Boolean
        fun downloadingModelFinished()
        fun startModel(itemId: Int)
        fun startModel(itemId: Int, image: ByteArray?)
        fun createGeneratorLoader(itemId: Int)
    }

    interface interactor {
        fun addModelsToNavBar(param: GeneratorListener)

        fun getModelFromFirebase(saveLocation: File, filenameInFirebase: String): FileDownloadTask
        fun showProgressOfFirebaseDownload(firebaseDownloader: FileDownloadTask)
    }

}
