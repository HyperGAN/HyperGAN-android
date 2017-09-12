package hypr.a255bits.com.hypr.MultiModels

import android.support.v4.app.FragmentManager
import hypr.a255bits.com.hypr.Generator.Generator
import java.io.File


interface MultiMvp{
    interface view{
        fun  startModelList(adapter: MultiModelAdapter?)

    }
    interface presenter{
        fun  startModelsList(generators: Array<Generator>?, fragmentManager: FragmentManager, image: String?, file: File)
        fun lockModel(indexOfFragment: Int)

        fun unlockModel(indexOfFragment: Int)
    }
}