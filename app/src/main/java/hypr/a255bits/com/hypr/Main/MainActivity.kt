package hypr.a255bits.com.hypr.Main

import android.app.ProgressDialog
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.SubMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import hypr.a255bits.com.hypr.Generator
import hypr.a255bits.com.hypr.ModelFragmnt.ModelFragment
import hypr.a255bits.com.hypr.Network.ModelDownloader
import hypr.a255bits.com.hypr.R
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.app_bar_main2.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.progressDialog
import java.io.File

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MainMvp.view {

    val interactor by lazy { MainInteractor(applicationContext) }
    val presenter by lazy { MainPresenter(this, interactor, applicationContext) }
    private var modelSubMenu: SubMenu? = null
    var progressDownloadingModel: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        presenter.addModelsToNavBar()
        setSupportActionBar(toolbar)
        setupDrawer(toolbar)

        startModelFragment("")
    }

    fun setupDrawer(toolbar: Toolbar) {
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        val navMenu = navigationView?.menu
        modelSubMenu = navMenu?.addSubMenu("Models")
    }

    override fun startModelFragment(modelUrl: String) {
        val fragment: Fragment = ModelFragment.newInstance(modelUrl, "")
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .disallowAddToBackStack()
                .commit()

    }

    override fun modeToNavBar(generator: Generator, index: Int) {
        modelSubMenu?.add(R.id.group1, index, index, generator.name)
        modelSubMenu?.getItem(index)?.setIcon(R.drawable.ic_lock)

    }

    override fun displayModelDownloadProgress() {
        progressDownloadingModel = progressDialog("Downloading Model") {
            setMessage("It's downloading..")
            setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            max = 100
        }
        progressDownloadingModel?.show()

    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId in 0..100) {
            presenter.startModel(item.itemId)
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showModelDownloadProgress(progressPercent: java.lang.Float) {
        progressDownloadingModel?.progress = progressPercent.toInt()

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}
