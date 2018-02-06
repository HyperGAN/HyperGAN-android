package hypr.gan.com.hypr.Main

import android.content.Context
import android.view.MenuItem
import com.google.android.gms.common.api.GoogleApiClient
import hypr.gan.com.hypr.BuyGenerator
import hypr.gan.com.hypr.Dashboard.DashboardFragment
import hypr.gan.com.hypr.Generator.Generator
import hypr.gan.com.hypr.ModelFragmnt.ModelFragment
import hypr.gan.com.hypr.R
import hypr.gan.com.hypr.Util.Analytics
import hypr.gan.com.hypr.Util.AnalyticsEvent
import hypr.gan.com.hypr.Util.ImageSaver
import hypr.gan.com.hypr.Util.InAppBilling.IabResult
import hypr.gan.com.hypr.Util.SettingsHelper
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.toast
import java.io.File

class MainPresenter(val view: MainMvp.view, val interactor: MainInteractor, val context: Context) : MainMvp.presenter {

    val ZERO_PERCENT: Float = 0.0f
    val SIGN_INTO_GOOGLE_RESULT: Int = 12
    val modelFileNames = listOf("expression-model.pb", "hd-face.pb").map {
        File(context.filesDir, it).absolutePath
    }

    private val DOWNLOAD_COMPLETE: Float = 100.0f
    var buyGenerators: MutableList<BuyGenerator> = mutableListOf()
    val analytics by lazy { Analytics(context) }
    var dashboard: DashboardFragment? = null
    var isModelFragmentDisplayed: Boolean = false
    var indexInJson: Int? = 0
    var image: String? = null
    var fullImage: String? = null
    val settingsHelper = SettingsHelper(context)
    var addModel: Job? = null
    var onBackPressed: Boolean? = false
    var isDoneLoading = false

    init {
        interactor.presenter = this
        if (settingsHelper.isModelImageRestoreable()) {
            image = settingsHelper.getModelImagePath()
            fullImage = settingsHelper.getFullImagePath()
        }
    }

    private fun restorePreviousUsedImage(file: File?) {
        image = file?.path
    }

    override fun handlePurchase(result: IabResult, generatorIndex: Int) {
        if (result.isSuccess) {
            dashboard?.presenter?.unlockBoughtModel(generatorIndex)
        } else {
            context.toast("Network error")
        }
    }

    override fun signInToGoogle(googleSignInClient: GoogleApiClient) {
        view.popupSigninGoogle(googleSignInClient)
    }

    override fun getModelFragment(position: Int): ModelFragment? {
        val generator = interactor.listOfGenerators?.get(position)
        val modelPbFile = File(modelFileNames[position])
        return generator?.let { ModelFragment.newInstance(it, image, modelPbFile, position, fullImage) }
    }

    override fun createGeneratorLoader(fileName: String, itemId: Int) {
        /*val file = File(fileName)
        if (!file.exists()) {
            val pbFilePointer = interactor.getModelFromFirebase(file, "optimized_weight_conv.pb")
            pbFilePointer?.addOnSuccessListener { taskSnapshot ->
                analytics.logEvent(AnalyticsEvent.GENERATOR_DOWNLOAD)
            }
        }*/

        displayMultiModels(itemId, null, interactor.listOfGenerators)
    }

    override fun buyModel(skus: String, generatorIndex: Int) {
        if (interactor.googleSignInClient.client.isConnected && !interactor.hasBoughtItem(skus)) {
            view.buyModelPopup(skus, interactor.billingHelper, generatorIndex)
        } else if (interactor.hasBoughtItem(skus)) {
            context.toast("You already bought this item.")
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
            createGeneratorLoader(modelFileNames[0], itemId)
        }
    }

    override fun createMultiModels(itemId: Int, image: String?) {
        val generator = interactor.listOfGenerators?.get(itemId)
        if (generator != null) {
            displayMultiModels(itemId, image, interactor.listOfGenerators)
        }
    }

    private fun displayMultiModels(itemId: Int, imageLocationPath: String?, listOfGenerators: List<Generator>?) {
        if (onBackPressed == false) {

            val multiModel = DashboardFragment.newInstance(listOfGenerators, itemId, imageLocationPath, modelFileNames.toTypedArray(), fullImage, false)
            this.dashboard = multiModel
            view.startMultipleModels(multiModel)
        } else {
            val multiModel = DashboardFragment.newInstance(listOfGenerators, itemId, imageLocationPath, modelFileNames.toTypedArray(), fullImage, true)
            this.dashboard = multiModel
            view.startMultipleModels(multiModel)
        }
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


    override fun addModelsToNavBar(applicationContext: Context) {
        addModel = launch(UI) {
            val generators = interactor.getGeneratorsFromNetwork(applicationContext).await()
            saveGeneratorInfo(generators)
            buyGenerators = mutableListOf()
            if (image != null) {
                image?.let { createMultiModels(indexInJson!!, it) }
            } else {
                view.displayGeneratorsOnHomePage(buyGenerators)
            }
            isDoneLoading = true
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

    fun stop() {
        dashboard = null
        addModel?.cancel()
    }

    fun listenForAppStartupForDecidingToRateAppPopup() {
        interactor.rateAppIfMeetConditions()
    }

    fun startFragment(fragmentTransaction: android.support.v4.app.FragmentTransaction) {
        if (isDoneLoading) {
            view.startFragment(fragmentTransaction)
        }
    }


}