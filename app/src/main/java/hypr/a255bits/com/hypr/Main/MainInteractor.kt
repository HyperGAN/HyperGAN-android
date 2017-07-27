package hypr.a255bits.com.hypr.Main

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.SparseArray
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.face.Face
import com.google.android.gms.vision.face.FaceDetector
import hypr.a255bits.com.hypr.Generator
import hypr.a255bits.com.hypr.Network.ModelApi
import hypr.a255bits.com.hypr.R
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainInteractor(val context: Context) : MainMvp.interactor {

    var listOfGenerators: List<Generator>? = null


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
        listOfGenerators?.forEachIndexed { index, generator ->
            param.getGenerator(generator, index)
        }
    }


}
