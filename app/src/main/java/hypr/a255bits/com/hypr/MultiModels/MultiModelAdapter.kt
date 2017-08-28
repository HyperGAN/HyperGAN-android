package hypr.a255bits.com.hypr.MultiModels

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import hypr.a255bits.com.hypr.Generator.Control
import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.ModelFragmnt.ModelFragment
import java.io.File

/**
 * Created by tedho on 8/28/2017.
 */

class MultiModelAdapter(fm: FragmentManager?, val generators: Array<Generator>, val image: String?, val file: File) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        val generator = generators[position]
        var fragment: Fragment? = null
        if (generator != null) {
            val controlArray: Array<Control>? = generator.generator?.viewer?.controls?.toTypedArray()
            fragment = image?.let { ModelFragment.newInstance(controlArray, it, file) }
        }
        return fragment
    }


    override fun getPageTitle(position: Int): CharSequence {
        return generators[position].name.toString()
    }

    override fun getCount(): Int {
        return generators.size
    }

}