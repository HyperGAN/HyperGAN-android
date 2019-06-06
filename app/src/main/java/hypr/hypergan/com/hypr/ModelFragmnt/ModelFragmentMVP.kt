package hypr.hypergan.com.hypr.ModelFragmnt

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.view.MenuItem
import hypr.hypergan.com.hypr.GeneratorLoader.FaceLocation
import hypr.hypergan.com.hypr.GeneratorLoader.Person

interface ModelFragmentMVP{
    interface view{
        fun displayFocusedImage(imageFromGallery: Bitmap?)
        fun loading(disableSlider: Boolean=true)
        fun  shareImageToOtherApps(shareIntent: Intent)
        fun  requestPermissionFromUser(permissions: Array<String>, REQUEST_CODE: Int)
        fun startCameraActivity()
        fun lockModel()
        fun unLockModel()
        fun rateApp()
        fun openRateAppInPlayStore(uri: Uri?, playStoreLink: Uri)
        fun displayedImageAsBitmap(): Bitmap
    }
    interface presenter{
        fun getFaceCroppedOutOfImageIfNoFaceGetFullImage(imageWithFaces: Bitmap?, context: Context): Bitmap?
        fun disconnectFaceDetector()
        fun saveImageDisplayedToPhone(context: Context): Boolean
        fun  changePixelToBitmap(transformedImage: IntArray): Bitmap?
        fun onRequestPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray, context: Context)
        fun  onOptionsItemSelected(item: MenuItem, context: Context)
        fun  readImageToBytes(imagePath: String?): ByteArray?
        fun randomizeModel(progress: Int)
        fun sampleImage(person: Person, image: Bitmap?, croppedPoint: Rect): Bitmap?
    }
    interface interactor{
        fun  getFacesFromBitmap(imageWithFaces: Bitmap, width: Int, height: Int, context: Context): MutableList<FaceLocation>
        fun  getIntentForSharingImagesWithOtherApps(imageFromGallery: Bitmap?): Intent
        fun  checkIfPermissionGranted(permission: String): Boolean
    }
}

