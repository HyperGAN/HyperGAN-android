package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.MenuItem
import hypr.a255bits.com.hypr.GeneratorLoader.FaceLocation

interface ModelFragmentMVP{
    interface view{
        fun displayFocusedImage(imageFromGallery: Bitmap?)
        fun  showError(errorMesssage: String)
        fun  shareImageToOtherApps(shareIntent: Intent)
        fun  requestPermissionFromUser(permissions: Array<String>, REQUEST_CODE: Int)
        fun startCameraActivity()
        fun lockModel()
        fun unLockModel()
    }
    interface presenter{
        fun getFaceCroppedOutOfImageIfNoFaceGetFullImage(imageWithFaces: Bitmap?, context: Context): Bitmap?
        fun disconnectFaceDetector()
        fun saveImageDisplayedToPhone(context: Context): Boolean
        fun  changePixelToBitmap(transformedImage: IntArray): Bitmap?
        fun sampleImage(image: Bitmap?): Bitmap
        fun onRequestPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
        fun  onOptionsItemSelected(item: MenuItem, context: Context)
        fun  readImageToBytes(imagePath: String?): ByteArray?
        fun randomizeModel(progress: Int)
    }
    interface interactor{
        fun  getFacesFromBitmap(imageWithFaces: Bitmap, width: Int, height: Int, context: Context): MutableList<FaceLocation>
        fun  getIntentForSharingImagesWithOtherApps(imageFromGallery: Bitmap?): Intent
        fun  checkIfPermissionGranted(permission: String): Boolean
    }
}

