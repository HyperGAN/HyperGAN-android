package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.MenuItem
import kotlinx.coroutines.experimental.Deferred
import java.io.File

interface ModelFragmentMVP{
    interface view{
        fun displayFocusedImage(imageFromGallery: Bitmap)
        fun  showError(errorMesssage: String)
        fun  shareImageToOtherApps(shareIntent: Intent)
        fun  requestPermissionFromUser(permissions: Array<String>, REQUEST_CODE: Int)
        fun displayTitleSpinner(actions: List<String?>?)
    }
    interface presenter{
        fun findFacesInImage(imageWithFaces: Bitmap, context: Context)
        fun disconnectFaceDetector()
        fun saveImageDisplayedToPhone(context: Context): Deferred<Boolean>?
        fun transformImage(normalImage: Bitmap?, pbFile: File?)
        fun  convertToNegative1To1(progress: Int): Double
        fun  changePixelToBitmap(transformedImage: IntArray): Bitmap?
        fun sampleImage(imageFromGallery: Bitmap): Deferred<IntArray>
        fun onRequestPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
        fun  onOptionsItemSelected(item: MenuItem, context: Context)
        fun displayTitleSpinner()
        fun  readImageToBytes(imagePath: String?)
    }
    interface interactor{
        fun  getFacesFromBitmap(imageWithFaces: Bitmap, width: Int, height: Int, context: Context): MutableList<Bitmap>
        fun  getIntentForSharingImagesWithOtherApps(imageFromGallery: Bitmap?): Intent
        fun  checkIfPermissionGranted(permission: String): Boolean
    }
}

