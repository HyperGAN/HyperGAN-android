package hypr.a255bits.com.hypr.MultiModels

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.R
import kotlinx.android.synthetic.main.fragment_multi_models.*
import java.io.File


class MultiModels : Fragment(), MultiMvp.view {

    private var generators: Array<Generator>? = null
    private var indexOfGenerator: Int? = null
    private var  pathToGenerator: String? = null
    private var image: String? = null
    val presenter: MultiPresenter by lazy { MultiPresenter(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            generators = arguments.getParcelableArray(GENERATORS) as Array<Generator>?
            indexOfGenerator = arguments.getInt(INDEX_OF_GENERATOR_IN_USE)
            image = arguments.getString(PATH_TO_IMAGE)
            pathToGenerator = arguments.getString(PATH_TO_GENERATOR)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_multi_models, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.startModelsList(generators, fragmentManager, image, File(pathToGenerator))
    }

    override fun startModelList(adapter: MultiModelAdapter?) {
        viewpager.adapter = adapter
        viewpager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                adapter?.addControlNamesToToolbar(adapter.generators[position])
            }

        })
        sliding_tabs.setupWithViewPager(viewpager)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        private val GENERATORS = "param1"
        private val INDEX_OF_GENERATOR_IN_USE = "param2"
        private val PATH_TO_IMAGE: String? = "pathtoImage"
        private val PATH_TO_GENERATOR: String? = "pathtoGenerator"

        fun newInstance(generators: List<Generator>?, indexOfGenerator: Int, pathToImage: String, generatorPath: File): MultiModels {
            val fragment = MultiModels()
            val args = Bundle()
            args.putParcelableArray(GENERATORS, generators?.toTypedArray())
            args.putInt(INDEX_OF_GENERATOR_IN_USE, indexOfGenerator)
            args.putString(PATH_TO_IMAGE, pathToImage)
            args.putString(PATH_TO_GENERATOR, generatorPath.absolutePath)
            fragment.arguments = args
            return fragment
        }
    }
}
