package hypr.a255bits.com.hypr.Main

import android.content.Context
import android.view.MenuItem
import com.google.android.gms.common.api.GoogleApiClient
import hypr.a255bits.com.hypr.BuyGenerator
import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.MultiModels.MultiModels
import hypr.a255bits.com.hypr.R
import hypr.a255bits.com.hypr.Util.Analytics
import hypr.a255bits.com.hypr.Util.AnalyticsEvent
import hypr.a255bits.com.hypr.Util.ImageSaver
import hypr.a255bits.com.hypr.Util.InAppBilling.IabResult
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.collections.forEachWithIndex
import java.io.File

class MainPresenter(val view: MainMvp.view, val interactor: MainInteractor, val context: Context) : MainMvp.presenter {

    val ZERO_PERCENT: Float = -0.0f
    val SIGN_INTO_GOOGLE_RESULT: Int = 12
    val file = File(context.filesDir, "optimized_weight_conv.pb")
    private val DOWNLOAD_COMPLETE: Float = 100.0f
    var buyGenerators: MutableList<BuyGenerator> = mutableListOf()
    val analytics by lazy { Analytics(context) }
    var multiModel: MultiModels? = null
    var isModelFragmentDisplayed: Boolean = false
    var indexInJson: Int? = null
    var image: ByteArray? = null

    init {
        interactor.presenter = this
    }

    override fun handlePurchase(result: IabResult, generatorIndex: Int) {
        if (result.isSuccess) {
            multiModel?.presenter?.unlockModel(generatorIndex)
        } else {
            println("buy error: $result")
        }
    }

    override fun signInToGoogle(googleSignInClient: GoogleApiClient) {
        view.popupSigninGoogle(googleSignInClient)
    }

    override fun createGeneratorLoader(file: File, itemId: Int) {
        if (!file.exists()) {
            val pbFilePointer = interactor.getModelFromFirebase(file, "optimized_weight_conv.pb")
            pbFilePointer?.addOnSuccessListener { taskSnapshot ->
                analytics.logEvent(AnalyticsEvent.GENERATOR_DOWNLOAD)
            }
        }
        view.startCameraActivity(itemId)
    }

    override fun disableModelsIfNotBought(listOfGenerators: List<Generator>?) {
        launch(UI) {
            listOfGenerators?.forEachWithIndex { index, generator ->
                val isModelBought = interactor.hasBoughtItem(generator.google_play_id)
                if (!isModelBought.await()) {
                    multiModel?.presenter?.lockModel(index)
                }
            }
        }
    }

    override fun buyModel(skus: String, generatorIndex: Int) {
        if (interactor.googleSignInClient.client.isConnected) {
            view.buyModelPopup(skus, interactor.billingHelper, generatorIndex)
        } else {
            signInToGoogle(interactor.googleSignInClient.client)
        }
    }

    override fun stopInAppBilling() {
        interactor.stopInAppBilling()
    }

    override fun onOptionsItemSelected(item: MenuItem?) {
        when (item?.itemId) {
            android.R.id.home -> {
                view.goBackToMainActivity()
            }
        }
    }

    override fun startModel(itemId: Int) {
        val generator = interactor.listOfGenerators?.get(itemId)
        if (generator != null) {
            createGeneratorLoader(file, itemId)
        }
    }

    override fun attemptToStartModel(itemId: Int) {
        interactor.attemptToStartModel(itemId)
    }

    override fun createMultiModels(itemId: Int, image: ByteArray?) {
        val generator = interactor.listOfGenerators?.get(itemId)
        if (generator != null) {
            val imageLocation = saveImageSoOtherFragmentCanViewIt(image)
            displayMultiModels(itemId, imageLocation.path, interactor.listOfGenerators)
        }
        disableModelsIfNotBought(interactor.listOfGenerators)
    }

    private fun displayMultiModels(itemId: Int, imageLocationPath: String, listOfGenerators: List<Generator>?) {
        multiModel = MultiModels.newInstance(listOfGenerators, itemId, imageLocationPath, file)
        view.startMultipleModels(multiModel!!)
        view.displayBackButton()
    }

    fun saveImageSoOtherFragmentCanViewIt(image: ByteArray?): File {
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
            val generators = interactor.getGeneratorsFromNetwork()
            saveGeneratorInfo(generators.await())
            if (isModelFragmentDisplayed) {
                indexInJson?.let { createMultiModels(it, image) }
            } else {
                view.displayGeneratorsOnHomePage(buyGenerators)
            }
        }
    }

    private fun saveGeneratorInfo(generators: List<Generator>?) {
        buyGenerators = mutableListOf()
        generators?.forEachIndexed { index, generator ->
            val buyGenerator = BuyGenerator(generator.name)
            buyGenerators.add(buyGenerator)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem) {
        if (item.itemId in 0..100) {
//            attemptToStartModel(item.itemId)
        } else if (item.itemId == R.id.homeButton) {
            view.displayGeneratorsOnHomePage(buyGenerators)
            analytics.logEvent(AnalyticsEvent.CHOOSE_HOME_NAV_OPTION)
        }
        analytics.logEvent(AnalyticsEvent.CHOOSE_SIDE_NAV_OPTION)
    }


}