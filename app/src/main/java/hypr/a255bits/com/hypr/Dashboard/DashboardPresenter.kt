package hypr.a255bits.com.hypr.Dashboard

import hypr.a255bits.com.hypr.BuyGenerator
import hypr.a255bits.com.hypr.Generator.Generator
import org.greenrobot.eventbus.EventBus

class DashboardPresenter(val view: DashboardMVP.view) {
    var buyGenerators: MutableList<BuyGenerator> = mutableListOf()
    fun displayListOfModels(generators: Array<Generator>) {
        generators.forEachIndexed { index, generator ->
            val buyGenerator = BuyGenerator(generator.name)
            buyGenerators.add(index, buyGenerator)
            val hash = hashMapOf("Generator" to generator.google_play_id, "Index" to index.toString())
            EventBus.getDefault().post(hash)
        }
    }
}
