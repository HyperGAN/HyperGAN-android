package hypr.a255bits.com.hypr.CameraFragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.net.Uri
import collections.forEach
import hypr.a255bits.com.hypr.Util.*


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
        if (facesDetected != null && facesDetected.size() > 0) {
            when {
                facesDetected.size() > 1 -> {
                    val faceLocations = mutableListOf<PointF>()
                    facesDetected.forEach { i, face -> faceLocations.add(face.position) }
                    view.startMultiFaceSelection(jpeg, faceLocations)
                }
                facesDetected.size() == 1 -> {
                    val images = faceDetection.getListOfFaces(facesDetected, bitmap)
                    SettingsHelper(context).saveFaceLocation(images[0].faceLocation)
                    SettingsHelper(context).setFaceIndex(0)
                    view.sendImageToModel(jpeg, images[0].croppedFace)}
                facesDetected.size() == 0 -> view.noFaceDetectedPopup()
            }
        }
    }


    override fun getImageFromImageFileLocation(imageLocation: Uri) {
        val imageFromGallery: ByteArray? = ImageSaver().uriToByteArray(imageLocation, context)
        val bitmap = BitmapFactory.decodeByteArray(imageFromGallery, 0, imageFromGallery!!.size)
        startModelIfImageHasFace(bitmap, imageFromGallery)
    }

    fun captureImage() {
        if (view.isCameraViewEnabled()) {
            view.takePicture()
            analytics.logEvent(AnalyticsEvent.TAKE_PICTURE)
        }
    }

    override fun displayGallery() {
        view.displayGallery()
        analytics.logEvent(AnalyticsEvent.EXISTING_PHOTO)
    }
}