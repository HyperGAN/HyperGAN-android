package hypr.a255bits.com.hypr

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MainMvp.view {

    val interactor by lazy { MainInteractor(applicationContext) }
    val presenter by lazy { MainPresenter(this, interactor, applicationContext) }
    val galleryFileLocation: Uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private val RESULT_GET_IMAGE: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.nav_camera) {
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
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
        focusedImage.setImageBitmap(imageFromGallery)
        println("going to crop")
    }
}
