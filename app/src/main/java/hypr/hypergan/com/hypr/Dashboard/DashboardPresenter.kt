package hypr.hypergan.com.hypr.Dashboard

import android.content.Context
import android.graphics.BitmapFactory
import hypr.hypergan.com.hypr.BuyGenerator
import hypr.hypergan.com.hypr.Generator.Generator
import org.greenrobot.eventbus.EventBus

class DashboardPresenter(val view: DashboardMVP.view) {
    var buyGenerators: MutableList<BuyGenerator> = mutableListOf()
    var adapter: WelcomeScreenAdapter? = null
    var generators: Array<Generator> = arrayOf()
    var indexOfGenerator: Int? = null
    var pathToGenerators: Array<String?> = arrayOf()
    var image: String? = null
    var fullImage: String? = null
    var isBackPressed: Boolean = false

    fun displayListOfModels(context: Context) {
        buyGenerators.clear()
        generators.forEachIndexed { index, generator ->
            val buyGenerator = BuyGenerator(generator.name)
            buyGenerators.add(index, buyGenerator)
            isItemBought(generator, index)
        }
        adapter = WelcomeScreenAdapter(buyGenerators, context, generators.map { BitmapFactory.decodeResource(context.resources, context.resources.getIdentifier(it.image, "drawable", context.packageName))})
        view.displayListOfModels(buyGenerators, adapter!!)
    }

    fun isItemBought(generator: Generator, index: Int) {
        val hash = hashMapOf("Generator" to generator.google_play_id, "Index" to index.toString())
        EventBus.getDefault().post(hash)
    }

    fun unlockBoughtModel(index: Int) {
        buyGenerators[index].itemBought = true
    }

    fun startModelIfFullImageIsPresent() {
        if(fullImage != null){
            EventBus.getDefault().post(indexOfGenerator?.toDouble())
        }
    }

    fun refreshList() {
        adapter?.notifyDataSetChanged()
    }


}
