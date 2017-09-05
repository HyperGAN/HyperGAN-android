package hypr.a255bits.com.hypr.MultiModels

import android.support.v4.app.FragmentManager
import hypr.a255bits.com.hypr.Generator.Generator
import java.io.File


class MultiPresenter(val view: MultiMvp.view): MultiMvp.presenter{
    override fun startModelsList(generators: Array<Generator>?, fragmentManager: FragmentManager, image: String?, file: File) {
        val adapter = generators?.let { MultiModelAdapter(fragmentManager, it, image, file) }
        view.startModelList(adapter)
    }

}