package hypr.hypergan.com.hypr.MultiModels

import android.support.v4.app.FragmentManager
import hypr.hypergan.com.hypr.Generator.Generator


interface MultiMvp{
    interface view{
        fun  startModelList(adapter: MultiModelAdapter?)

    }
    interface presenter{
        fun  startModelsList(generators: Array<Generator>?, fragmentManager: FragmentManager, image: String?, filePaths: Array<String?>, fullImage: String?)
        fun lockModel(indexOfFragment: Int)

        fun unlockModel(indexOfFragment: Int)
    }
}