package hypr.a255bits.com.hypr.ModelFragmnt

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import com.pawegio.kandroid.onProgressChanged
import com.pawegio.kandroid.setSize
import hypr.a255bits.com.hypr.Generator.Control
import hypr.a255bits.com.hypr.GeneratorLoader

import hypr.a255bits.com.hypr.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main2.*
import org.jetbrains.anko.toast
import java.io.File
import java.util.*


class ModelFragment : Fragment(), ModelFragmentMVP.view {

    var pbFile: File? = null
    private var modelUrl: Array<Control>? = null
    private var image: ByteArray? = null
    val interactor by lazy { ModelInteractor(context) }
    val presenter by lazy { ModelFragmentPresenter(this, interactor, context) }
    val generatorLoader = GeneratorLoader()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            modelUrl = arguments.getParcelableArray(MODEL_CONTROLS) as Array<Control>?
            image = arguments.getByteArray(IMAGE_PARAM)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_model, container, false)
        displayTitleSpinner()
        setHasOptionsMenu(true)
        return view
    }

    fun displayTitleSpinner() {
        activity.toolbar.title = ""
        val actions = listOf("Smile", "Frown")
        val adapter: SpinnerAdapter = ArrayAdapter<String>(activity, R.layout.spinner_dropdown_item, actions)
        val spinner = Spinner(activity)
        spinner.background.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        spinner.adapter = adapter
        activity.toolbar.addView(spinner, 0)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayImageTransitionSeekbarProgress()
        presenter.loadGenerator(generatorLoader, pbFile)
        val imageBitmap = image?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
        presenter.transformImage(imageBitmap, pbFile, generatorLoader)


    }

    private fun displayImageTransitionSeekbarProgress() {
        imageTransitionSeekBar.onProgressChanged { progress, _ ->
            val ganValue: Double = presenter.convertToNegative1To1(progress)
            println("oldValue: $progress")
            println("actualyValue: $ganValue")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.image_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.saveImage -> presenter.saveImageDisplayedToPhone()
            R.id.shareIamge -> presenter.shareImageToOtherApps()
        }
        return super.onOptionsItemSelected(item)
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

    override fun displayFocusedImage(imageFromGallery: Bitmap) {
        val scaled = Bitmap.createScaledBitmap(imageFromGallery, 128, 128, false)
        val encoded = generatorLoader.encode(scaled)
        focusedImage.setImageBitmap(generatorLoader.sample(encoded))
    }

    override fun shareImageToOtherApps(shareIntent: Intent) {
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_image)))
    }

    override fun requestPermissionFromUser(permissions: Array<String>, REQUEST_CODE: Int) {
        requestPermissions(permissions, REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        grantResults.filter { item -> item == PackageManager.PERMISSION_GRANTED }.forEach { item ->
            if (requestCode == presenter.SHARE_IMAGE_PERMISSION_REQUEST) {
                presenter.shareImageToOtherApps()
            }
        }

    }

    companion object {
        private val IMAGE_PARAM = "param2"

        private val  MODEL_CONTROLS = "modelControls"

        fun newInstance(modelControls: Array<Control>?, image: ByteArray?, pbFile: File): ModelFragment {
            val fragment = ModelFragment()
            val args = Bundle()
            args.putByteArray(IMAGE_PARAM, image)
            args.putParcelableArray(MODEL_CONTROLS, modelControls)
            fragment.arguments = args
            fragment.pbFile = pbFile
            return fragment
        }
    }

}
