package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.MenuItem

interface ModelFragmentMVP{
    interface view{
        fun displayFocusedImage(imageFromGallery: Bitmap?)
        fun  showError(errorMesssage: String)
        fun  shareImageToOtherApps(shareIntent: Intent)
        fun  requestPermissionFromUser(permissions: Array<String>, REQUEST_CODE: Int)
        fun startCameraActivity()
        fun lockModel()
        fun unLockModel()
        fun changeGanImageFromSlider(ganValue: Double)
    }
    interface presenter{
        fun findFacesInImage(imageWithFaces: Bitmap?, context: Context)
        fun disconnectFaceDetector()
        fun saveImageDisplayedToPhone(context: Context): Boolean
        fun transformImage(normalImage: Bitmap?)
        fun  changePixelToBitmap(transformedImage: IntArray): Bitmap?
        fun sampleImage(image: Bitmap?): Bitmap
        fun onRequestPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
        fun  onOptionsItemSelected(item: MenuItem, context: Context)
        fun displayTitleSpinner()
        fun  readImageToBytes(imagePath: String?)
        fun convertByteArrayImageToBitmap(): Bitmap?
        fun randomizeModel(progress: Int)
    }
    interface interactor{
        fun  getFacesFromBitmap(imageWithFaces: Bitmap, width: Int, height: Int, context: Context): MutableList<Bitmap>
        fun  getIntentForSharingImagesWithOtherApps(imageFromGallery: Bitmap?): Intent
        fun  checkIfPermissionGranted(permission: String): Boolean
    }
}

