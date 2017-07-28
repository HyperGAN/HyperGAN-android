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

    }
    interface presenter{

        fun addModelsToNavBar()
        fun  startModel(itemId: Int)
    }

    interface interactor {
        fun addModelsToNavBar(param: GeneratorListener)

        fun getModelFromFirebase(saveLocation: File, filename: String): FileDownloadTask
        fun showProgressOfFirebaseDownload(firebaseDownloader: FileDownloadTask)
    }

}
