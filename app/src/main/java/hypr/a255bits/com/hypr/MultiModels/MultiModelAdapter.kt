package hypr.a255bits.com.hypr.MultiModels

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import hypr.a255bits.com.hypr.Generator.Control
import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.ModelFragmnt.ModelFragment
import org.greenrobot.eventbus.EventBus
import java.io.File


class MultiModelAdapter(fm: FragmentManager?, val generators: Array<Generator>, val image: String?, val file: File, val fullImage: String?) : FragmentPagerAdapter(fm) {
    val modelFragments = mutableListOf<ModelFragment>()
    init{
       addControlNamesToToolbar(generators[0])
    }
    override fun getItem(position: Int): Fragment? {
        val generator = generators[position]
        val modelFragment = createFragment(generator, position)
        modelFragment?.let { modelFragments.add(it) }
        return modelFragment
    }

    fun addControlNamesToToolbar(generator: Generator) {
        val controlNames: List<String?>? = generator.generator?.viewer?.controls?.map { control -> control.name }
        EventBus.getDefault().post(controlNames)
    }

    fun createFragment(generator: Generator, position: Int): ModelFragment? {
        val controlArray: Array<Control>? = generator.generator?.viewer?.controls?.toTypedArray()
        return generator.let {  ModelFragment.newInstance(it, image, file, position, fullImage)}
    }

    override fun getPageTitle(position: Int): CharSequence {
        return generators[position].name
    }

    override fun getCount(): Int {
        return generators.size
    }

    fun lockModelFromIndex(indexOfFragment: Int) {
        modelFragments[indexOfFragment].lockModel()
    }

    fun  unlockModelFromIndex(indexOfFragment: Int) {
        modelFragments[indexOfFragment].unLockModel()
    }

}