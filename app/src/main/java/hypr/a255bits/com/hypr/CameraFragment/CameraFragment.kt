package hypr.a255bits.com.hypr.CameraFragment

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import hypr.a255bits.com.hypr.R
import kotlinx.android.synthetic.main.app_bar_main2.*
import kotlinx.android.synthetic.main.app_bar_main2.view.*
import kotlinx.android.synthetic.main.fragment_camera.*

class CameraFragment : Fragment() {

    private var modelUrl: String? = null
    private var mParam2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            modelUrl = arguments.getString(MODEL_URL)
            mParam2 = arguments.getString(ARG_PARAM2)
        }

    }

    override fun onStart() {
        super.onStart()
        cameraView.start()
    }

    override fun onPause() {
        super.onPause()
        cameraView.stop()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_camera, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        activity.actionBar
//                .setBackgroundDrawable(ColorDrawable(Color.parseColor("#00ffffff")))
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }


    companion object {
        private val MODEL_URL = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): CameraFragment {
            val fragment = CameraFragment()
            val args = Bundle()
            args.putString(MODEL_URL, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
