package hypr.a255bits.com.hypr

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import kotlin.properties.Delegates
import android.graphics.BitmapFactory



class MainActivity : AppCompatActivity(), MainMvp.view {

    val interactor by lazy { MainInteractor(applicationContext) }
    val presenter by lazy { MainPresenter(this, interactor, applicationContext) }
    val galleryFileLocation: Uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    var focusedImageBitmap: Bitmap? = null

    private val RESULT_GET_IMAGE: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        val imageByteArray: ByteArray = savedInstanceState?.get("image") as ByteArray
        val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
        restoreImage(bitmap)
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        val byteArrayImage = getByteArrayFromBitmap(focusedImageBitmap)
        outState?.putByteArray("image", byteArrayImage)
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        focusedImage.setImageBitmap(null)
    }

    private fun  restoreImage(bitmap: Bitmap?) {
        focusedImageBitmap = bitmap
        focusedImage.setImageBitmap(bitmap)
    }

    private fun  getByteArrayFromBitmap(focusedImageBitmap: Bitmap?): ByteArray {
        val stream = ByteArrayOutputStream()
        focusedImageBitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.image_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.saveImage -> presenter.saveImageToInternalStorage(focusedImageBitmap)
        }
        return super.onOptionsItemSelected(item)
    }

    fun chooseImageClick(view: View) {
        presenter.displayGallery()

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

    override fun displayFocusedImage(imageFromGallery: Bitmap) {
        this.focusedImageBitmap = imageFromGallery
        focusedImage.setImageBitmap(imageFromGallery)
        println("going to crop")
    }

}
