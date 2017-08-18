package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import hypr.a255bits.com.hypr.GeneratorLoader
import kotlinx.coroutines.experimental.Deferred
import java.io.File

interface ModelFragmentMVP{
    interface view{
        fun displayFocusedImage(imageFromGallery: Bitmap)
        fun  showError(errorMesssage: String)
        fun  shareImageToOtherApps(shareIntent: Intent)
        fun  requestPermissionFromUser(permissions: Array<String>, REQUEST_CODE: Int)
    }
    interface presenter{
        fun findFacesInImage(imageWithFaces: Bitmap, context: Context)
        fun disconnectFaceDetector()
        fun saveImageDisplayedToPhone(context: Context): Deferred<Boolean>
        fun transformImage(normalImage: Bitmap?, pbFile: File?, generatorLoader: GeneratorLoader)
        fun  convertToNegative1To1(progress: Int): Double
    }
    interface interactor{
        fun  getFacesFromBitmap(imageWithFaces: Bitmap, width: Int, height: Int, context: Context): MutableList<Bitmap>
        fun  getIntentForSharingImagesWithOtherApps(imageFromGallery: Bitmap?): Intent
        fun  checkIfPermissionGranted(permission: String): Boolean
    }
}

