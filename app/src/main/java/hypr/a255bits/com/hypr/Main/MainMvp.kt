package hypr.a255bits.com.hypr.Main

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import hypr.a255bits.com.hypr.Generator

interface MainMvp {
    interface view{
        fun  modeToNavBar(generator: Generator, index: Int)
        fun  startModelFragment(modelUrl: String)

    }
    interface presenter{

        fun addModelsToNavBar()
        fun  startModel(itemId: Int)
    }

    interface interactor {
        fun addModelsToNavBar(param: GeneratorListener)

    }

}
