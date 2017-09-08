package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import com.pawegio.kandroid.onProgressChanged
import hypr.a255bits.com.hypr.CameraFragment.CameraActivity
import hypr.a255bits.com.hypr.Generator.Control
import hypr.a255bits.com.hypr.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.io.File


class ModelFragment : Fragment(), ModelFragmentMVP.view {

    var pbFile: File? = null
    val interactor by lazy { ModelInteractor(context) }
    val presenter by lazy { ModelFragmentPresenter(this, interactor, context, pbFile) }
    var direction: FloatArray? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            presenter.modelUrl = arguments.getParcelableArray(MODEL_CONTROLS) as Array<Control>?
            presenter.readImageToBytes(arguments.getString(IMAGE_PARAM))
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_model, container, false)
        presenter.displayTitleSpinner()
        setHasOptionsMenu(true)
        return view
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayImageTransitionSeekbarProgress()
        randomizeModel.setOnClickListener { direction = presenter.generatorLoader.random_z() }
        chooseImageFromGalleryButton.setOnClickListener {
            presenter.startCameraActivity()
        }
    }

    override fun startCameraActivity() {
        val intent = activity.intentFor<CameraActivity>("indexInJson" to 0)
        EventBus.getDefault().post(intent)
    }

    private fun displayImageTransitionSeekbarProgress() {
        imageTransitionSeekBar.onProgressChanged { progress, _ ->
            val ganValue: Double = presenter.convertToNegative1To1(progress)
            changeGanImageFromSlider(ganValue)
            println("oldValue: $progress")
            println("actualyValue: $ganValue")
        }
    }

    private fun changeGanImageFromSlider(ganValue: Double) {
        presenter.encoded?.let {
            val direction = this.direction ?: presenter.generatorLoader.random_z()
            val ganImage = presenter.generatorLoader.sample(it, ganValue.toFloat(), presenter.mask, direction, presenter.baseImage!!)
            val z_slider = presenter.generatorLoader.get_z(it, ganValue.toFloat(), it)

            Log.d("z_slider", z_slider[0].toString())
            focusedImage.setImageBitmap(presenter.generatorLoader.manipulateBitmap(presenter.generatorLoader.width, presenter.generatorLoader.height, ganImage))


        }


    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.image_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        presenter.onOptionsItemSelected(item, context)
        return super.onOptionsItemSelected(item)
    }

    override fun onDetach() {
        super.onDetach()
        presenter.disconnectFaceDetector()
    }

    override fun showError(errorMesssage: String) {
        context.toast(errorMesssage)
    }

    override fun displayFocusedImage(imageFromGallery: Bitmap) {
        launch(UI) {
            val transformedImage = presenter.sampleImage(imageFromGallery).await()
            focusedImage.setImageBitmap(transformedImage)
        }
    }


    override fun shareImageToOtherApps(shareIntent: Intent) {
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_image)))
    }

    override fun requestPermissionFromUser(permissions: Array<String>, REQUEST_CODE: Int) {
        requestPermissions(permissions, REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        presenter.onRequestPermissionResult(requestCode, permissions, grantResults)
    }

    companion object {
        private val IMAGE_PARAM = "param2"

        private val MODEL_CONTROLS = "modelControls"

        fun newInstance(modelControls: Array<Control>?, image: String, pbFile: File): ModelFragment {
            val fragment = ModelFragment()
            val args = Bundle()
            args.putString(IMAGE_PARAM, image)
            args.putParcelableArray(MODEL_CONTROLS, modelControls)
            fragment.arguments = args
            fragment.pbFile = pbFile
            return fragment
        }
    }

}
