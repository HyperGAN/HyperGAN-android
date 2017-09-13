package hypr.a255bits.com.hypr.Main

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.Network.ModelApi
import hypr.a255bits.com.hypr.Network.ModelDownloader
import hypr.a255bits.com.hypr.R
import hypr.a255bits.com.hypr.Util.InAppBilling.IabHelper
import hypr.a255bits.com.hypr.Util.InAppBilling.Inventory
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.coroutines.experimental.bg
import java.io.File

class MainInteractor(val context: Context) : MainMvp.interactor {

    var presenter: MainPresenter? = null

    var inappBillingEnabled = true
    var billingHelper: IabHelper = IabHelper(context, context.getString(R.string.API_KEY))
    val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
    }
    val googleSignInClient: GoogleApiClient by lazy {
        GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    var listOfGenerators: List<Generator>? = null
    var modelDownloader = ModelDownloader(FirebaseStorage.getInstance().reference)

    init {
        if (inappBillingEnabled) {
            startInAppBilling()
        }
    }

    private fun startInAppBilling() {
        billingHelper.startSetup { result ->
            if (!result.isSuccess) {
                inappBillingEnabled = true
                presenter?.signInToGoogle(googleSignInClient)
            } else {
                Log.d("MainInteractor", "Problem setting up In-app Billing: $result")
            }
        }
    }

    override fun hasBoughtItem(itemId: String): Deferred<Boolean> {
        return if (inappBillingEnabled) {
            async(UI) {
                val inventory = query(true, mutableListOf(itemId), null).await()
                return@async inventory.hasPurchase(itemId)
            }
        } else {
            async(UI) {
                return@async true
            }
        }
    }

    override fun attemptToStartModel(itemId: Int) {
        launch(UI) {
            val productId = listOfGenerators?.get(itemId)?.google_play_id
            productId?.let {
                val hasBoughtItem = hasBoughtItem(it).await()
                if (hasBoughtItem) {
                    presenter?.startModel(itemId)
                } else {
                    buyProduct(it)
                }
            }
        }
    }

    suspend fun buyProduct(productId: String) {
        if (inappBillingEnabled) {
            val skus = mutableListOf(productId)
            val inventory = query(true, skus, null).await()
            if (!inventory.hasPurchase(productId)) {
                presenter?.buyModel(productId, billingHelper)
            }
        }
    }

    fun query(query: Boolean, skus: MutableList<String>, moreSubsSkus: List<String>?): Deferred<Inventory> {
        if (inappBillingEnabled) {
            return async(UI) {
                billingHelper.queryInventory(true, skus, null)
            }
        } else {
            return async(UI) { Inventory() }
        }
    }

    override fun stopInAppBilling() {
        billingHelper.dispose()
    }

    override fun getModelFromFirebase(saveLocation: File, filenameInFirebase: String): FileDownloadTask? {
        val firebaseGeneratorPath = listOfGenerators?.get(0)?.model_url
        return firebaseGeneratorPath?.let {
            modelDownloader.getFile(saveLocation, it)
        }

    }

    override fun showProgressOfFirebaseDownload(firebaseDownloader: FileDownloadTask) {
        firebaseDownloader.addOnProgressListener { taskSnapshot ->
            showDownloadProgress(taskSnapshot.bytesTransferred, taskSnapshot.totalByteCount)
        }
    }

    private fun showDownloadProgress(bytesTransferred: Long, totalByteCount: Long) {
        val percent: Float = (bytesTransferred * 100.0f) / totalByteCount
        EventBus.getDefault().post(percent)

    }

    override fun getGeneratorsFromNetwork(): Deferred<List<Generator>?> {
        return async(UI) {
            val modelApi = ModelApi()
            val listOfModels = modelApi.listOfModels()
            val listOfGenerators = bg {
                listOfModels?.execute()?.body()
            }.await()
            this@MainInteractor.listOfGenerators = listOfGenerators
            listOfGenerators

        }
    }
}
