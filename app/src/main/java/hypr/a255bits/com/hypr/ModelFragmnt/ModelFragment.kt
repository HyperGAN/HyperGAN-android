package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import com.pawegio.kandroid.onProgressChanged
import hotchemi.android.rate.AppRate
import hypr.a255bits.com.hypr.CameraFragment.CameraActivity
import hypr.a255bits.com.hypr.DependencyInjection.GeneratorModule
import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.R
import hypr.a255bits.com.hypr.Util.negative1To1
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.intentFor
import org.koin.android.contextaware.ContextAwareFragment
import java.io.File


class ModelFragment : ContextAwareFragment(), ModelFragmentMVP.view {

    override val contextName: String
        get() = "generator"

    var pbFile: File? = null
    val presenter by lazy { ModelFragmentPresenter(GeneratorModule().getGeneratorLoader()) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.setInteractors(ModelInteractor(context as Context))
        presenter.setViews(this)
        presenter.easyGenerator.assets = context?.assets
        //presenter.easyGenerator.loadAssets(context as Context)
        presenter.getInfoFromFragmentCreation(arguments as Bundle)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater!!.inflate(R.layout.fragment_model, container, false)
    }

    override fun rateApp() {
        AppRate.showRateDialogIfMeetsConditions(activity)
    }

    override fun openRateAppInPlayStore(marketLink: Uri?, playStoreLink: Uri) {
        val goToMarket = Intent(Intent.ACTION_VIEW, marketLink)
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, playStoreLink))
        }
    }

    override fun lockModel() {
        lockLayout.visibility = View.VISIBLE
        imageTransitionSeekBar.isEnabled = false
    }

    override fun unLockModel() {
        lockLayout.visibility = View.INVISIBLE
        imageTransitionSeekBar.isEnabled = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingIcon.show()
        presenter.loadGenerator(context as Context, pbFile)
        displayImageTransitionSeekbarProgress()
        randomizeModelClickListener()
        chooseImageFromGalleryButtonClickListener()
        lockLayoutClickListener()
    }

    private fun randomizeModelClickListener() {

        randomizeModel.setOnClickListener {
            this.loading()
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
            activity?.alert(getString(R.string.buy_model_popup_message), "Hypr") {
                positiveButton("Buy", { EventBus.getDefault().post(presenter.generatorIndex) })
                cancelButton { dialog -> dialog.dismiss() }
            }?.show()
        }
    }

    override fun startCameraActivity() {
        val intent = activity?.intentFor<CameraActivity>("indexInJson" to presenter.generatorIndex)
        EventBus.getDefault().post(intent)
    }

    private fun displayImageTransitionSeekbarProgress() {
        imageTransitionSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            var progress: Double = 0.0

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                this.progress = progress.negative1To1()
                val progress = this.progress
                GlobalScope.async(Dispatchers.Main) {
                    presenter.changeGanImageFromSlider(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                presenter.view.loading(false)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val progress = this.progress

                GlobalScope.async(Dispatchers.Main) {
                    presenter.changeGanImageFromSlider(progress)
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.image_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        presenter.onOptionsItemSelected(item, context as Context)
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        presenter.disconnectFaceDetector()
    }

    override fun displayFocusedImage(imageFromGallery: Bitmap?) {
        focusedImage.let { imageFromGallery.let { focusedImage.setImageBitmap(it) } }
        loadingIcon.hide()
        imageTransitionSeekBar.setEnabled(true)
        randomizeModel.setEnabled(true)
    }
    override fun loading(disableSlider: Boolean) {
        if(disableSlider) {
            imageTransitionSeekBar.setEnabled(false)
            randomizeModel.setEnabled(false)
        }
        loadingIcon.show()

    }

    override fun shareImageToOtherApps(shareIntent: Intent) {
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_image)))
    }

    override fun requestPermissionFromUser(permissions: Array<String>, REQUEST_CODE: Int) {
        requestPermissions(permissions, REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        presenter.onRequestPermissionResult(requestCode, permissions, grantResults, context as Context)
    }

    companion object {
        val IMAGE_PARAM = "param2"
        val MODEL_CONTROLS = "modelControls"
        val GENERATOR_INDEX = "generatorPosition"
        val FULL_IMAGE_LOCATION = "fulliamgelocation"


        fun newInstance(generator: Generator, image: String?, pbFile: File, generatorIndex: Int, fullImage: String?): ModelFragment {
            val fragment = ModelFragment()
            val args = Bundle()
            args.putString(IMAGE_PARAM, image)
            args.putString(FULL_IMAGE_LOCATION, fullImage)
            args.putParcelable(MODEL_CONTROLS, generator)
            args.putInt(GENERATOR_INDEX, generatorIndex)
            fragment.arguments = args
            fragment.pbFile = pbFile
            return fragment
        }
    }

}
