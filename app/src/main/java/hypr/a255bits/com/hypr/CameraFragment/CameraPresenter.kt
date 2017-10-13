package hypr.a255bits.com.hypr.CameraFragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import hypr.a255bits.com.hypr.Util.Analytics
import hypr.a255bits.com.hypr.Util.AnalyticsEvent
import hypr.a255bits.com.hypr.Util.FaceDetection
import hypr.a255bits.com.hypr.Util.ImageSaver


class CameraPresenter(val view: CameraMVP.view, val context: Context) : CameraMVP.presenter {

    val interactor: CameraInteractor by lazy { CameraInteractor(context) }
    val analytics = Analytics(context)
    val faceDetection = FaceDetection(context)
    override fun sendPictureToModel(jpeg: ByteArray?) {
        val bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg!!.size)
        startModelIfImageHasFace(bitmap, jpeg)

    }

    private fun startModelIfImageHasFace(bitmap: Bitmap?, jpeg: ByteArray) {
        val facesDetected = bitmap?.let { faceDetection.getFaceLocations(it, context) }
        if (facesDetected != null) {
            when {
                facesDetected.get(0) != null -> view.sendImageToModel(jpeg)
                facesDetected.size() > 1 -> {view.startMultiFaceSelection(jpeg, facesDetected)}
                else -> view.noFaceDetectedPopup()
            }
        }
    }


    override fun getImageFromImageFileLocation(imageLocation: Uri) {
        val imageFromGallery: ByteArray? = ImageSaver().uriToByteArray(imageLocation, context)
        val bitmap = BitmapFactory.decodeByteArray(imageFromGallery, 0, imageFromGallery!!.size)
        startModelIfImageHasFace(bitmap, imageFromGallery)
    }

    fun captureImage() {
        view.takePicture()
        analytics.logEvent(AnalyticsEvent.TAKE_PICTURE)
    }

    override fun displayGallery() {
        view.displayGallery()
        analytics.logEvent(AnalyticsEvent.EXISTING_PHOTO)
    }
}