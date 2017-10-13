package hypr.a255bits.com.hypr.CameraFragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.util.SparseArray
import android.view.View
import com.flurgle.camerakit.CameraView
import com.google.android.gms.vision.face.Face
import com.pawegio.kandroid.start
import hypr.a255bits.com.hypr.Main.MainActivity
import hypr.a255bits.com.hypr.MultiFaceSelection.MultiFaceSelection
import hypr.a255bits.com.hypr.R
import hypr.a255bits.com.hypr.Util.ImageSaver
import hypr.a255bits.com.hypr.Util.onPictureTaken
import kotlinx.android.synthetic.main.activity_camera.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast
import java.io.File

class CameraActivity : AppCompatActivity(), CameraMVP.view {

    val presenter: CameraPresenter by lazy { CameraPresenter(this, applicationContext) }
    private val RESULT_GET_IMAGE: Int = 1
    var indexInJson: Int? = null
    val galleryFileLocation: Uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        setSupportActionBar(toolbar)
        indexInJson = intent.extras.getInt("indexInJson")
        takePictureListener(cameraView)
        takePicture.setOnClickListener { presenter.captureImage() }
    }

    override fun noFaceDetectedPopup() {
        longToast("Please select an image with a face")
    }

    fun galleryButtonClick(view: View) {
        presenter.displayGallery()
    }

    fun switchCameraClick(view: View){
        cameraView.toggleFacing()
    }

    override fun takePicture() {
        cameraView.captureImage()
    }

    override fun startMultiFaceSelection(jpeg: ByteArray, facesDetected: SparseArray<Face>) {
        intentFor<MultiFaceSelection>("image" to jpeg, "faceLocations" to facesDetected).start(applicationContext)

    }

    private fun takePictureListener(cameraView: CameraView) {
        cameraView.onPictureTaken { jpeg -> presenter.sendPictureToModel(jpeg) }
    }

    override fun navigateUpActivity() {
        NavUtils.navigateUpFromSameTask(this)
    }

    override fun displayGallery() {
        val intent = Intent(Intent.ACTION_PICK, galleryFileLocation)
        startActivityForResult(intent, RESULT_GET_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == RESULT_GET_IMAGE && resultCode == Activity.RESULT_OK) {
            intent?.data?.let { presenter.getImageFromImageFileLocation(it) }
        }
    }

    override fun onResume() {
        super.onResume()
            cameraView.start()
    }

    override fun onPause() {
        super.onPause()
        cameraView.stop()
    }

    override fun sendImageToModel(image: ByteArray?) {
        val file = saveImageSoOtherFragmentCanViewIt(image)
        startActivity(intentFor<MainActivity>
        ("indexInJson" to indexInJson, "image" to file.path).clearTop())
    }
    fun saveImageSoOtherFragmentCanViewIt(image: ByteArray?): File {
        val file = File.createTempFile("image", "png")
        ImageSaver().saveImageToFile(file, image)
        return file

    }
}
