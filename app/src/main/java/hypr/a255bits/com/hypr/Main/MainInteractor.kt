package hypr.a255bits.com.hypr.Main

import android.content.Context
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.Network.ModelApi
import hypr.a255bits.com.hypr.Network.ModelDownloader
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.coroutines.experimental.bg
import java.io.File

class MainInteractor(val context: Context) : MainMvp.interactor {

    var listOfGenerators: List<Generator>? = null
    var modelDownloader = ModelDownloader(FirebaseStorage.getInstance().reference)


    override fun getModelFromFirebase(saveLocation: File, filenameInFirebase: String): FileDownloadTask {
        return modelDownloader.getFile(saveLocation, filenameInFirebase)

    }
    override fun showProgressOfFirebaseDownload(firebaseDownloader: FileDownloadTask){
        firebaseDownloader.addOnProgressListener { taskSnapshot ->
           showDownloadProgress(taskSnapshot.bytesTransferred, taskSnapshot.totalByteCount)
        }
    }

    private fun showDownloadProgress(bytesTransferred: Long, totalByteCount: Long) {
        val percent: Float = (bytesTransferred * 100.0f) / totalByteCount
        EventBus.getDefault().post(percent)

    }

    override fun addModelsToNavBar(param: GeneratorListener) {
        if (listOfGenerators == null) {
            getGeneratorFromNetwork(param)
        } else {
            callListenerForEachGenerator(param, listOfGenerators)
        }
    }

    private fun getGeneratorFromNetwork(param: GeneratorListener) {
        val modelApi = ModelApi()
        val listOfModels = modelApi.listOfModels()
        async(UI) {
            val listOfGenerators = bg {
                listOfModels?.execute()?.body()
            }.await()
            this@MainInteractor.listOfGenerators = listOfGenerators
            callListenerForEachGenerator(param, listOfGenerators)
        }
    }

    private fun callListenerForEachGenerator(param: GeneratorListener, listOfGenerators: List<Generator>?) {
        listOfGenerators?.let { param.getGenerators(it, 0) }
    }


}
