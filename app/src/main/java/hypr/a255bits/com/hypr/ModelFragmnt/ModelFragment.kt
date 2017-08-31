package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import com.pawegio.kandroid.onProgressChanged
import com.pawegio.kandroid.toast
import hypr.a255bits.com.hypr.Generator.Control
import hypr.a255bits.com.hypr.GeneratorLoader.GeneratorLoader
import hypr.a255bits.com.hypr.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main2.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.toast
import java.io.File


class ModelFragment : Fragment(), ModelFragmentMVP.view {

    var pbFile: File? = null
    private var modelUrl: Array<Control>? = null
    private var image: String = ""
    val interactor by lazy { ModelInteractor(context) }
    val presenter by lazy { ModelFragmentPresenter(this, interactor, context) }
    val generatorLoader = GeneratorLoader()
    var encoded: FloatArray? = null
    var mask: FloatArray? = null
    var baseImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            modelUrl = arguments.getParcelableArray(MODEL_CONTROLS) as Array<Control>?
            image = arguments.getString(IMAGE_PARAM)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_model, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayImageTransitionSeekbarProgress()
        presenter.loadGenerator(generatorLoader, pbFile)
        val byteArrayImage = File(image).readBytes()

        val imageBitmap = BitmapFactory.decodeByteArray(byteArrayImage, 0, byteArrayImage.size)
        presenter.transformImage(imageBitmap, pbFile, generatorLoader)
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
        encoded?.let {
            val direction = generatorLoader.random_z()
            val ganImage = generatorLoader.sample(it, ganValue.toFloat(), mask, direction, baseImage!!)
            val z_slider = generatorLoader.get_z(it, ganValue.toFloat(), it)

            Log.d("z_slider", z_slider[0].toString())
            focusedImage.setImageBitmap(generatorLoader.manipulateBitmap(generatorLoader.width, generatorLoader.height, ganImage))

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.image_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        launch(UI) {
            when (item.itemId) {
                R.id.saveImage -> {
                    bg { presenter.saveImageDisplayedToPhone(getContext()) }.await()
                    toast("Image Saved!")
                }
                R.id.shareIamge -> presenter.shareImageToOtherApps()
            }
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
        val scaled = Bitmap.createScaledBitmap(imageFromGallery, 256, 256, false)
        baseImage = scaled

        encoded = generatorLoader.encode(scaled)

        mask = generatorLoader.mask(scaled)
        val direction = generatorLoader.random_z()
        val transformedImage = generatorLoader.sample(encoded!!, 0.0f, mask, direction, baseImage!!)
        focusedImage.setImageBitmap(generatorLoader.manipulateBitmap(generatorLoader.width, generatorLoader.height, transformedImage))
    }

    override fun changePixelToBitmap(image: IntArray): Bitmap {
        return generatorLoader.manipulateBitmap(generatorLoader.width, generatorLoader.height, image)


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
