package hypr.a255bits.com.hypr.CameraFragment

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.view.MenuItem
import com.flurgle.camerakit.CameraListener
import hypr.a255bits.com.hypr.Main.MainActivity

import hypr.a255bits.com.hypr.R
import kotlinx.android.synthetic.main.activity_camera.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor

class CameraActivity : AppCompatActivity(), CameraMVP.view {

    val presenter: CameraPresenter by lazy { CameraPresenter(this) }

    var indexInJson: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        setSupportActionBar(toolbar)
        indexInJson = intent.extras.getInt("indexInJson")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        cameraView.setCameraListener(object : CameraListener() {
            override fun onPictureTaken(jpeg: ByteArray?) {
                super.onPictureTaken(jpeg)
                presenter.sendPictureToModel(jpeg)
            }
        })
        takePicture.setOnClickListener {
            cameraView.captureImage()
        }

    }

    override fun onStart() {
        super.onStart()
        cameraView.start()
    }

    override fun onPause() {
        super.onPause()
        cameraView.stop()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> NavUtils.navigateUpFromSameTask(this)
        }
        return super.onOptionsItemSelected(item)

    }

    override fun sendImageToModel(image: ByteArray?) {
        startActivity(intentFor<MainActivity>
        ("indexInJson" to indexInJson, "image" to image).clearTop())
    }
}
