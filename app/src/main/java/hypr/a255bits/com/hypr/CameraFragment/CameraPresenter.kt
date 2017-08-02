package hypr.a255bits.com.hypr.CameraFragment

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri


class CameraPresenter(val view: CameraMVP.view, val context: Context) : CameraMVP.presenter {
    val interactor: CameraInteractor by lazy{CameraInteractor(context)}
    var shouldLoadCamera = true
    override fun sendPictureToModel(jpeg: ByteArray?) {
        view.sendImageToModel(jpeg)
    }

    override fun getImageFromImageFileLocation(imageLocation: Uri) {
        val imageFromGallery: ByteArray? = interactor.uriToByteArray(imageLocation)
        view.sendImageToModel(imageFromGallery)
    }

}