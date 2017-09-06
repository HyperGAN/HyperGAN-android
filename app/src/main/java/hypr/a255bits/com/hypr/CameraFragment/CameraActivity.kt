package hypr.a255bits.com.hypr.CameraFragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.view.MenuItem
import android.view.View
import com.flurgle.camerakit.CameraListener
import com.flurgle.camerakit.CameraView
import hypr.a255bits.com.hypr.Main.MainActivity

import hypr.a255bits.com.hypr.R
import kotlinx.android.synthetic.main.activity_camera.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor

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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        takePictureListener(cameraView)
    }

    fun takePictureButtonClick(view: View) {
        cameraView.captureImage()
    }

    fun galleryButtonClick(view: View) {
        displayGallery()
    }

    private fun takePictureListener(cameraView: CameraView) {
        cameraView.setCameraListener(object : CameraListener() {
            override fun onPictureTaken(jpeg: ByteArray?) {
                super.onPictureTaken(jpeg)
                presenter.sendPictureToModel(jpeg)
            }
        })
    }

    override fun navigateUpActivity() {
        NavUtils.navigateUpFromSameTask(this)
    }

    override fun displayGallery() {
        presenter.shouldLoadCamera = false
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
        if (presenter.shouldLoadCamera) {
            cameraView.start()
        }
    }

    override fun onPause() {
        super.onPause()
        cameraView.stop()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        presenter.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)

    }

    override fun sendImageToModel(image: ByteArray?) {
        startActivity(intentFor<MainActivity>
        ("indexInJson" to indexInJson, "image" to image).clearTop())
    }
}
