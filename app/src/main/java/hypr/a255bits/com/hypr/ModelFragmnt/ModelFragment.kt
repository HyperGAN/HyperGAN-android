package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.pawegio.kandroid.onProgressChanged
import hypr.a255bits.com.hypr.CameraFragment.CameraActivity
import hypr.a255bits.com.hypr.Generator.Control
import hypr.a255bits.com.hypr.R
import hypr.a255bits.com.hypr.Util.negative1To1
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_model.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.*
import org.jetbrains.anko.coroutines.experimental.bg
import java.io.File


class ModelFragment : Fragment(), ModelFragmentMVP.view {

    var pbFile: File? = null
    val interactor by lazy { ModelInteractor(context) }
    val presenter by lazy { ModelFragmentPresenter(this, interactor, context, pbFile) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            presenter.modelUrl = arguments.getParcelableArray(MODEL_CONTROLS) as Array<Control>?
            presenter.readImageToBytes(arguments.getString(IMAGE_PARAM))
            presenter.generatorIndex = arguments.getInt(GENERATOR_INDEX)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        presenter.displayTitleSpinner()
        setHasOptionsMenu(true)
        return inflater!!.inflate(R.layout.fragment_model, container, false)
    }

    override fun lockModel() {
        lockLayout.visibility = View.VISIBLE
        imageTransitionSeekBar.isEnabled = false
    }

    override fun unLockModel() {
        lockLayout.visibility = View.INVISIBLE
        imageTransitionSeekBar.isEnabled = true

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayImageTransitionSeekbarProgress()
        randomizeModelClickListener()
        chooseImageFromGalleryButtonClickListener()
        lockLayoutClickListener()
    }

    private fun randomizeModelClickListener() {
        randomizeModel.setOnClickListener {
            presenter.direction = presenter.generatorLoader.random_z()
            presenter.randomizeModel(imageTransitionSeekBar.progress)
        }
    }

    private fun chooseImageFromGalleryButtonClickListener() {
        chooseImageFromGalleryButton.setOnClickListener {
            presenter.startCameraActivity()
        }
    }

    private fun lockLayoutClickListener() {
        lockLayout.setOnClickListener {
            activity.alert("Would you like to buy this model?", "Hypr") {
                positiveButton("Buy", { EventBus.getDefault().post(presenter.generatorIndex) })
                cancelButton { dialog -> dialog.dismiss() }
            }.show()
        }
    }

    override fun startCameraActivity() {
        val intent = activity.intentFor<CameraActivity>("indexInJson" to 0)
        EventBus.getDefault().post(intent)
    }

    private fun displayImageTransitionSeekbarProgress() {
        imageTransitionSeekBar.onProgressChanged { progress, _ ->
            changeGanImageFromSlider(progress.negative1To1())
        }
    }

    override fun changeGanImageFromSlider(ganValue: Double) {
        presenter.encoded?.let {
            launch(UI) {
                val direction = presenter.direction ?: presenter.generatorLoader.random_z()
                val ganImage = presenter.generatorLoader.sample(it, ganValue.toFloat(), presenter.mask, direction, presenter.baseImage!!)
                val manipulatedBitmap = bg{presenter.generatorLoader.manipulateBitmap(presenter.generatorLoader.width, presenter.generatorLoader.height, ganImage)}
                focusedImage.setImageBitmap(manipulatedBitmap.await())
            }
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

    override fun displayFocusedImage(imageFromGallery: Bitmap?) {
        launch(UI) {
            val transformedImage = bg { presenter.sampleImage(imageFromGallery) }
            focusedImage.setImageBitmap(transformedImage.await())
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
        private val GENERATOR_INDEX = "generatorPosition"


        fun newInstance(modelControls: Array<Control>?, image: String?, pbFile: File, generatorIndex: Int): ModelFragment {
            val fragment = ModelFragment()
            val args = Bundle()
            args.putString(IMAGE_PARAM, image)
            args.putParcelableArray(MODEL_CONTROLS, modelControls)
            args.putInt(GENERATOR_INDEX, generatorIndex)
            fragment.arguments = args
            fragment.pbFile = pbFile
            return fragment
        }
    }

}
