package hypr.a255bits.com.hypr.Main

import android.content.Context
import android.view.MenuItem
import com.google.android.gms.common.api.GoogleApiClient
import hypr.a255bits.com.hypr.BuyGenerator
import hypr.a255bits.com.hypr.Generator.Control
import hypr.a255bits.com.hypr.R
import hypr.a255bits.com.hypr.Util.ImageSaver
import hypr.a255bits.com.hypr.Util.InAppBilling.IabHelper
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.io.File

class MainPresenter(val view: MainMvp.view, val interactor: MainInteractor, val context: Context) : MainMvp.presenter {

    val file = File(context.filesDir, "optimized_weight_conv.pb")
    private val DOWNLOAD_COMPLETE: Float = 100.0f
    var isLoggedIntoGoogle: Boolean = false
    var buyGenerators: MutableList<BuyGenerator> = mutableListOf()

    init {
        interactor.presenter = this
    }

    override fun signInToGoogle(googleSignInClient: GoogleApiClient) {
        view.popupSigninGoogle(googleSignInClient)
    }

    override fun createGeneratorLoader(file: File, itemId: Int) {
        if (!file.exists()) {
            val pbFilePointer = interactor.getModelFromFirebase(file, "optimized_weight_conv.pb")
            pbFilePointer?.let { interactor.showProgressOfFirebaseDownload(it) }
            pbFilePointer?.addOnSuccessListener { taskSnapshot ->
                println("successs")
                view.startCameraActivity(itemId)
            }
        } else {
            view.startCameraActivity(itemId)
        }
    }

    override fun buyModel(skus: String, billingHelper: IabHelper?) {
        if (isLoggedIntoGoogle) {
            view.buyModelPopup(skus, billingHelper)
        } else {
            signInToGoogle(interactor.googleSignInClient)
        }
    }

    override fun stopInAppBilling() {
        interactor.stopInAppBilling()
    }

    override fun startModel(itemId: Int) {
        val generator = interactor.listOfGenerators?.get(itemId)
        if (generator != null) {
            createGeneratorLoader(file, itemId)
//            view.displayModelDownloadProgress()
//            val file = File.createTempFile("optimized_weight_conv", "pb")
//            val filePointer = interactor.getModelFromFirebase(file, "optimized_weight_conv.pb")
//            interactor.showProgressOfFirebaseDownload(filePointer)
        }
    }

    override fun attemptToStartModel(itemId: Int) {
        interactor.attemptToStartModel(itemId)
    }

    override fun startModel(itemId: Int, image: ByteArray?) {
        val generator = interactor.listOfGenerators?.get(itemId)
        if (generator != null) {
            val controlArray: Array<Control>? = generator.generator?.viewer?.controls?.toTypedArray()
            controlArray?.let {
                val imageLocation = saveImageSoOtherFragmentCanViewIt(image)
                view.applyModelToImage(it, image, imageLocation.path, interactor.listOfGenerators, itemId) }
        }
    }

    fun  saveImageSoOtherFragmentCanViewIt(image: ByteArray?): File {
        val file = File.createTempFile("image", "png")
        ImageSaver().saveImageToFile(file, image)
        return file

    }

    override fun downloadingModelFinished() {
        view.closeDownloadingModelDialog()
    }

    override fun isDownloadComplete(progressPercent: Float): Boolean {
        return progressPercent >= DOWNLOAD_COMPLETE
    }


    override fun addModelsToNavBar() {
        launch(UI) {
            val generators = interactor.getGeneratorsFromNetwork().await()
            buyGenerators = mutableListOf()
            generators?.forEachIndexed { index, generator ->
                view.addModelsToNavBar(generator, index)
                    saveGeneratorInfo(generator.name)
            }
            view.startModelOnImage(buyGenerators)
        }
    }

    private fun saveGeneratorInfo(name: String) {
        val buyGenerator = BuyGenerator(name)
        buyGenerators.add(buyGenerator)
    }

    override fun onNavigationItemSelected(item: MenuItem) {
        if (item.itemId in 0..100) {
           attemptToStartModel(item.itemId)

        } else if (item.itemId == R.id.homeButton) {
            view.displayGeneratorsOnHomePage(buyGenerators)
        }
    }
}