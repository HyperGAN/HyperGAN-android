package hypr.gan.com.hypr.Dashboard

import android.content.Context
import hypr.gan.com.hypr.BuyGenerator
import hypr.gan.com.hypr.Generator.Generator
import org.greenrobot.eventbus.EventBus

class DashboardPresenter(val view: DashboardMVP.view) {
    var buyGenerators: MutableList<BuyGenerator> = mutableListOf()
    var adapter: WelcomeScreenAdapter? = null

    fun displayListOfModels(generators: Array<Generator>, context: Context) {
        buyGenerators.clear()
        generators.forEachIndexed { index, generator ->
            val buyGenerator = BuyGenerator(generator.name)
            buyGenerators.add(index, buyGenerator)
            isItemBought(generator, index)
        }
        adapter = WelcomeScreenAdapter(buyGenerators, context)
        view.displayListOfModels(buyGenerators, adapter!!)
    }

    fun isItemBought(generator: Generator, index: Int) {
        val hash = hashMapOf("Generator" to generator.google_play_id, "Index" to index.toString())
        EventBus.getDefault().post(hash)
    }

    fun unlockBoughtModel(index: Int) {
        buyGenerators[index].itemBought = true
    }

    fun startModelIfFullImageIsPresent(fullImage: String?, indexOfGenerator: Int?) {
        if(fullImage != null){
            EventBus.getDefault().post(indexOfGenerator?.toDouble())
        }
    }

}
