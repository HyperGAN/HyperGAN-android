package hypr.hypergan.com.hypr.MultiModels

import android.support.v4.app.FragmentManager
import hypr.hypergan.com.hypr.Generator.Generator
import java.io.File


class MultiPresenter(val view: MultiMvp.view): MultiMvp.presenter{

    var adapter: MultiModelAdapter? = null
    override fun startModelsList(generators: Array<Generator>?, fragmentManager: FragmentManager, image: String?, paths: Array<String?>, fullImage: String?) {
        adapter = generators?.let { MultiModelAdapter(fragmentManager, it, image, File(paths[0]), fullImage) }
        view.startModelList(adapter)
    }

    override fun lockModel(indexOfFragment: Int) {
        adapter?.lockModelFromIndex(indexOfFragment)
    }
    override fun unlockModel(indexOfFragment: Int){
       adapter?.unlockModelFromIndex(indexOfFragment)
    }
}