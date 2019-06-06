package hypr.hypergan.com.hypr.Main

import android.content.Context
import android.support.v4.app.Fragment
import android.view.MenuItem
import com.google.android.gms.common.api.GoogleApiClient
import hypr.hypergan.com.hypr.Generator.Generator
import hypr.hypergan.com.hypr.ModelFragmnt.ModelFragment
import hypr.hypergan.com.hypr.Util.InAppBilling.IabHelper
import hypr.hypergan.com.hypr.Util.InAppBilling.IabResult
import kotlinx.coroutines.Deferred
import java.io.File

interface MainMvp {
    interface view {
        fun addModelsToNavBar(generator: Generator, index: Int)
        fun displayModelDownloadProgress()
        fun closeDownloadingModelDialog()
        fun startCameraActivity(indexInJson: Int)
        fun popupSigninGoogle(googleSignInClient: GoogleApiClient)
        fun  buyModelPopup(skus: String, billingHelper: IabHelper?, generatorIndex: Int)
        fun goBackToMainActivity()
        fun  displayBackButton()
        fun startFragment(fragmentTransaction: android.support.v4.app.FragmentTransaction)

        fun startFragment(fragment: Fragment)
    }

    interface presenter {
        fun addModelsToNavBar(applicationContext: Context)
        fun isDownloadComplete(progressPercent: Float): Boolean
        fun downloadingModelFinished()
        fun startModel(itemId: Int)
        fun createMultiModels(itemId: Int, image: String?)
        fun createGeneratorLoader(fileName: String, itemId: Int)
        fun stopInAppBilling()
        fun  signInToGoogle(googleSignInClient: GoogleApiClient)
        fun  buyModel(skus: String, generatorIndex: Int)
        fun onNavigationItemSelected(item: MenuItem)
        fun onOptionsItemSelected(item: MenuItem?)
        fun handlePurchase(result: IabResult, generatorIndex: Int)
        fun getModelFragment(position: Int): ModelFragment?
    }

    interface interactor {
        fun getGeneratorsFromNetwork(applicationContext: Context): Deferred<List<Generator>?>

        fun stopInAppBilling()
        fun hasBoughtItem(itemId: String): Boolean
    }

}
