package hypr.a255bits.com.hypr.Main

import android.content.Context
import android.util.Log
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import hypr.a255bits.com.hypr.DependencyInjection.GeneratorModule
import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.Network.ModelDownloader
import hypr.a255bits.com.hypr.R
import hypr.a255bits.com.hypr.Util.GoogleSignIn
import hypr.a255bits.com.hypr.Util.InAppBilling.IabHelper
import hypr.a255bits.com.hypr.Util.InAppBilling.Inventory
import hypr.a255bits.com.hypr.Util.JsonReader
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import java.io.File

class MainInteractor(val context: Context) : MainMvp.interactor {

    var presenter: MainPresenter? = null

    var billingHelper: IabHelper = IabHelper(context, context.getString(R.string.API_KEY))
    val googleSignInClient = GoogleSignIn(context)

    var modelDownloader = ModelDownloader(FirebaseStorage.getInstance().reference)
    val jsonReader = JsonReader(GeneratorModule().getJsonAdapter())
    var listOfGenerators: List<Generator>? = listOf()

    init {
        googleSignInClient.client.connect()
        startInAppBilling()
    }

    private fun startInAppBilling() {
        billingHelper.startSetup { result ->
            if (result.isSuccess) {
                billingHelper.isConnected = true
            } else {
                billingHelper.isConnected = false
                Log.d("IabHelper", "Problem setting up In-app Billing: $result")
            }
        }
    }

    override fun hasBoughtItem(itemId: String): Boolean {
        val hasBoughtItem = if (itemId.isEmpty()) {
            true
        } else {
            val inventory = query(true, mutableListOf(itemId), null)
            inventory.hasPurchase(itemId)
        }
        return hasBoughtItem
    }

    fun query(query: Boolean, skus: MutableList<String>, moreSubsSkus: List<String>?): Inventory {
        return if (billingHelper.isConnected) {
            billingHelper.queryInventory(true, skus, null)
        } else {
            Inventory()
        }
    }

    override fun stopInAppBilling() {
        billingHelper.disposeWhenFinished()
    }

    override fun getModelFromFirebase(saveLocation: File, filenameInFirebase: String): FileDownloadTask? {
        val firebaseGeneratorPath = listOfGenerators?.get(0)?.model_url
        return firebaseGeneratorPath?.let {
            modelDownloader.getFile(saveLocation, it)
        }

    }

    override fun getGeneratorsFromNetwork(applicationContext: Context): Deferred<List<Generator>?> {
        return async(UI) {
            val listOfGenerators = bg { jsonReader.getGeneratorsFromJson(applicationContext) }.await()
            this@MainInteractor.listOfGenerators = listOfGenerators
            return@async listOfGenerators
        }
    }
}
