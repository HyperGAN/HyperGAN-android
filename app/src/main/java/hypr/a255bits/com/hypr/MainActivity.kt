package hypr.a255bits.com.hypr

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity(), MainMvp.view {

    val interactor by lazy { MainInteractor(applicationContext) }
    val presenter by lazy { MainPresenter(applicationContext, this, interactor) }
    val galleryFileLocation: Uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private val RESULT_GET_IMAGE: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun chooseImageClick(view: View) {
        presenter.displayGallery()

    }


    override fun displayGallery() {
        val intent = Intent(Intent.ACTION_PICK, galleryFileLocation)
        startActivityForResult(intent, RESULT_GET_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_GET_IMAGE && resultCode == Activity.RESULT_OK) {
        }
    }

    override fun displayFocusedImage() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
