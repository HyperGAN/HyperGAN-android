package hypr.a255bits.com.hypr.MultiModels

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import hypr.a255bits.com.hypr.Generator.Control
import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.ModelFragmnt.ModelFragment
import org.greenrobot.eventbus.EventBus
import java.io.File


class MultiModelAdapter(fm: FragmentManager?, val generators: Array<Generator>, val image: String?, val file: File) : FragmentPagerAdapter(fm) {
    init{
       addControlNamesToToolbar(generators[0])
    }
    override fun getItem(position: Int): Fragment? {
        val generator = generators[position]
        return createFragment(generator)
    }

    fun addControlNamesToToolbar(generator: Generator) {
        val controlNames: List<String?>? = generator.generator?.viewer?.controls?.map { control -> control.name }
        EventBus.getDefault().post(controlNames)
    }

    fun createFragment(generator: Generator): ModelFragment? {
        val controlArray: Array<Control>? = generator.generator?.viewer?.controls?.toTypedArray()
        return image?.let { ModelFragment.newInstance(controlArray, it, file) }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return generators[position].name
    }

    override fun getCount(): Int {
        return generators.size
    }

}