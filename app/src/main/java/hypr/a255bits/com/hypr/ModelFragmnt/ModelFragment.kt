package hypr.a255bits.com.hypr.ModelFragmnt

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import hypr.a255bits.com.hypr.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast


class ModelFragment : Fragment(), ModelFragmentMVP.view {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    val galleryFileLocation: Uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private val RESULT_GET_IMAGE: Int = 1
    val interactor by lazy { ModelInteractor(context) }
    val presenter by lazy { ModelFragmentPresenter(this, interactor, context) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(MODEL_URL)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_model, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chooseImageFromGalleryButton.setOnClickListener { presenter.displayGallery() }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        presenter.disconnectFaceDetector()
    }

    override fun showError(errorMesssage: String) {
        context.toast(errorMesssage)
    }

    override fun displayGallery() {
        val intent = Intent(Intent.ACTION_PICK, galleryFileLocation)
        startActivityForResult(intent, RESULT_GET_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == RESULT_GET_IMAGE && resultCode == Activity.RESULT_OK) {
            intent?.data?.let { presenter.getImageFromImageFileLocation(it) }
        }
    }

    override fun displayFocusedImage(imageFromGallery: Bitmap) {
        focusedImage.setImageBitmap(imageFromGallery)
    }


    companion object {
        private val MODEL_URL = "param1"
        private val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): ModelFragment {
            val fragment = ModelFragment()
            val args = Bundle()
            args.putString(MODEL_URL, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}
