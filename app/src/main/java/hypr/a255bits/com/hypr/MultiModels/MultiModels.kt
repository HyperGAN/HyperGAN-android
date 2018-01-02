package hypr.a255bits.com.hypr.MultiModels

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.R
import hypr.a255bits.com.hypr.Util.onPageSelected
import kotlinx.android.synthetic.main.fragment_multi_models.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.collections.forEachWithIndex


class MultiModels : Fragment(), MultiMvp.view {

    private var generators: Array<Generator> = arrayOf()
    private var indexOfGenerator: Int? = null
    private var pathToGenerators: Array<String?> = arrayOf()
    private var image: String? = null
    private var fullImage: String? = null
    val presenter: MultiPresenter by lazy { MultiPresenter(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            generators = arguments.getParcelableArray(GENERATORS) as Array<Generator>
            indexOfGenerator = arguments.getInt(INDEX_OF_GENERATOR_IN_USE)
            image = arguments.getString(PATH_TO_IMAGE)
            fullImage = arguments.getString(PATH_TO_FULL_IMAGE)
            pathToGenerators = arguments.getStringArray(PATH_TO_GENERATORS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_multi_models, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.startModelsList(generators, fragmentManager, image, pathToGenerators, fullImage)
        disableModelsIfNotBought(generators)
    }

    fun disableModelsIfNotBought(listOfGenerators: Array<Generator>?) {
        launch(UI) {
            listOfGenerators?.forEachWithIndex { index, generator ->
                val hash = hashMapOf("Generator" to generator.google_play_id, "Index" to index.toString())
                EventBus.getDefault().post(hash)

            }
        }
    }

    override fun startModelList(adapter: MultiModelAdapter?) {
        viewpager.adapter = adapter
        viewpager.onPageSelected { position ->
            adapter?.addControlNamesToToolbar(adapter.generators[position])
        }
        sliding_tabs.setupWithViewPager(viewpager)
    }

    companion object {
        private val GENERATORS = "param1"
        private val INDEX_OF_GENERATOR_IN_USE = "param2"
        private val PATH_TO_IMAGE: String? = "pathtoImage"
        private val PATH_TO_GENERATORS: String? = "pathtoGenerator"
        private val PATH_TO_FULL_IMAGE: String = "pathtofullimage"

        fun newInstance(generators: List<Generator>?, indexOfGenerator: Int, pathToImage: String?, generatorPaths: Array<String>, fullImage: String?): MultiModels {
            val fragment = MultiModels()
            val args = Bundle()
            args.putParcelableArray(GENERATORS, generators?.toTypedArray())
            args.putInt(INDEX_OF_GENERATOR_IN_USE, indexOfGenerator)
            args.putString(PATH_TO_IMAGE, pathToImage)
            args.putStringArray(PATH_TO_GENERATORS, generatorPaths)
            args.putString(PATH_TO_FULL_IMAGE, fullImage)

            fragment.arguments = args
            return fragment
        }
    }
}
