package hypr.a255bits.com.hypr.ModelFragmnt

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.provider.MediaStore
import android.util.SparseArray
import com.google.android.gms.vision.face.Face
import com.pawegio.kandroid.fromApi
import hypr.a255bits.com.hypr.GeneratorLoader.FaceLocation
import hypr.a255bits.com.hypr.Util.Analytics
import hypr.a255bits.com.hypr.Util.FaceDetection
import hypr.a255bits.com.hypr.Util.SettingsHelper
import java.io.IOException
import android.support.v4.content.ContextCompat.startActivity
import com.pawegio.kandroid.BuildConfig
import com.pawegio.kandroid.start


class ModelInteractor(val context: Context, val faceDetection: FaceDetection) : ModelFragmentMVP.interactor {
    val analytics = Analytics(context)
    val settings = SettingsHelper(context)

    override fun checkIfPermissionGranted(permission: String): Boolean {
        var isPermissionGranted = true
        fromApi(23) {
            isPermissionGranted = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

        }
        return isPermissionGranted
    }

    override fun getIntentForSharingImagesWithOtherApps(imageFromGallery: Bitmap?): Intent {
        val pathToImage = MediaStore.Images.Media.insertImage(context.contentResolver, imageFromGallery, "title", null)
        val shareableIamge = Uri.parse(pathToImage)
        return createImageShareIntent(shareableIamge)
    }

    private fun createImageShareIntent(parse: Uri?): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/jpeg"
        shareIntent.putExtra(Intent.EXTRA_STREAM, parse)
        return shareIntent
    }

    fun rateApp(){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
        var sAux = "\nLet me recommend you this application\n\n"
        sAux += "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}\n\n"
        intent.putExtra(Intent.EXTRA_TEXT, sAux)
        val chooser = Intent.createChooser(intent, "choose one")
        chooser.start(context)

    }

    @Throws(IOException::class)
    override fun getFacesFromBitmap(imageWithFaces: Bitmap, width: Int, height: Int, context: Context): MutableList<FaceLocation> {
        val faceLocations: SparseArray<Face>? = faceDetection.getFaceLocations(imageWithFaces, context)
        return faceDetection.getListOfFaces(faceLocations, imageWithFaces, context)
    }

    fun placeWatermarkOnImage(bitmap: Bitmap?): Bitmap? {
        val paint = Paint()
        val textBounds = Rect()
        val text = "Hypr"
        paint.getTextBounds(text, 0, text.length, textBounds)
        val canvas = Canvas(bitmap)
        canvas.drawText("Hypr", canvas.width.toFloat() - (textBounds.width() * 2), canvas.height.toFloat() - (textBounds.height()), paint)
        return bitmap
    }

}
