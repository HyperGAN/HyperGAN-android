package hypr.a255bits.com.hypr.MultiFaceSelection

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hypr.a255bits.com.hypr.R
import hypr.a255bits.com.hypr.Util.toBitmap
import kotlinx.android.synthetic.main.fragment_multi_face.*

class MultiFaceFragment : Fragment(), MultiFaceMVP.view{

    private var imageOfPeoplesFaces: Bitmap? = null
    private var faceLocations: Array<PointF>? = null
    private val presenter by lazy{MultiFacePresenter(this)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            imageOfPeoplesFaces = arguments.getByteArray(IMAGE).toBitmap()
            faceLocations = arguments.getParcelableArray(FACE_LOCATIONS) as Array<PointF>?
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_multi_face, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.displayImageWithFaces(imageOfPeoplesFaces)
    }

    override fun displayImageWithFaces(imageOfPeople: Bitmap) {
        drawableImageView.bitmap = imageOfPeople

    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        private val IMAGE = "param1"
        private val FACE_LOCATIONS = "param2"

        fun newInstance(image: ByteArray?, faceLocations: Array<PointF>?): MultiFaceFragment {
            val fragment = MultiFaceFragment()
            val args = Bundle()
            args.putByteArray(IMAGE, image)
            args.putParcelableArray(FACE_LOCATIONS, faceLocations)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
