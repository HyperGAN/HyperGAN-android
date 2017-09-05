package hypr.a255bits.com.hypr.WelcomeScreen

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hypr.a255bits.com.hypr.BuyGenerator
import hypr.a255bits.com.hypr.R
import kotlinx.android.synthetic.main.fragment_welcome_screen.*


class WelcomeScreen : Fragment() {
    private var buyGenerators: Array<BuyGenerator>? = arrayOf()
    private var mParam2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            buyGenerators = arguments.getParcelableArray(ARG_PARAM1) as Array<BuyGenerator>
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_welcome_screen, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayListOfModels()
    }

    private fun displayListOfModels() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = buyGenerators?.let { WelcomeScreenAdapter(it) }
        recyclerView.adapter = adapter
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(buyGenerators: MutableList<BuyGenerator>, param2: String): WelcomeScreen {
            val fragment = WelcomeScreen()
            val args = Bundle()
            args.putParcelableArray(ARG_PARAM1, buyGenerators.toTypedArray())
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}