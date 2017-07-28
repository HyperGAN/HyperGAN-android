package hypr.a255bits.com.hypr.Main

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.storage.FileDownloadTask
import hypr.a255bits.com.hypr.Generator
import java.io.File

interface MainMvp {
    interface view{
        fun  modeToNavBar(generator: Generator, index: Int)
        fun  startModelFragment(modelUrl: String)
        fun displayModelDownloadProgress()
        fun closeDownloadingModelDialog()

    }
    interface presenter{

        fun addModelsToNavBar()
        fun  startModel(itemId: Int)
        fun  isDownloadComplete(progressPercent: Float): Boolean
        fun downloadingModelFinished()
    }

    interface interactor {
        fun addModelsToNavBar(param: GeneratorListener)

        fun getModelFromFirebase(saveLocation: File, filenameInFirebase: String): FileDownloadTask
        fun showProgressOfFirebaseDownload(firebaseDownloader: FileDownloadTask)
    }

}
