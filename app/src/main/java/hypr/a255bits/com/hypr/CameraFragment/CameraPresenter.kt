package hypr.a255bits.com.hypr.CameraFragment


class CameraPresenter(val view: CameraMVP.view): CameraMVP.presenter{
    override fun sendPictureToModel(jpeg: ByteArray?) {
        view.sendImageToModel(jpeg)
    }


}