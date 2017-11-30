package hypr.a255bits.com.hypr.Dashboard

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hypr.a255bits.com.hypr.BuyGenerator
import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.R
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment(), DashboardMVP.view {

    private var generators: Array<Generator> = arrayOf()
    private var indexOfGenerator: Int? = null
    private var pathToGenerators: Array<String?> = arrayOf()
    private var image: String? = null
    private var fullImage: String? = null
    val presenter by lazy{DashboardPresenter(this)}

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
        return inflater!!.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.displayListOfModels(generators, context)

    }

    override fun displayListOfModels(buyGenerators: MutableList<BuyGenerator>, welcomeScreenAdapter: WelcomeScreenAdapter) {
        recyclerview.layoutManager = LinearLayoutManager(activity)
        recyclerview.adapter = welcomeScreenAdapter
        welcomeScreenAdapter.notifyDataSetChanged()
    }

    companion object {
        private val GENERATORS = "param1"
        private val INDEX_OF_GENERATOR_IN_USE = "param2"
        private val PATH_TO_IMAGE: String? = "pathtoImage"
        private val PATH_TO_GENERATORS: String? = "pathtoGenerator"
        private val PATH_TO_FULL_IMAGE: String = "pathtofullimage"

        fun newInstance(generators: List<Generator>?, indexOfGenerator: Int, pathToImage: String?, generatorPaths: Array<String>, fullImage: String?): DashboardFragment {
            val fragment = DashboardFragment()
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
