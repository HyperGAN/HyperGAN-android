package hypr.a255bits.com.hypr.Main

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import hypr.a255bits.com.hypr.Generator
import hypr.a255bits.com.hypr.GeneratorLoader
import hypr.a255bits.com.hypr.ModelFragmnt.ModelFragment
import hypr.a255bits.com.hypr.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.app_bar_main2.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MainMvp.view {

    val interactor by lazy { MainInteractor(applicationContext) }
    val presenter by lazy { MainPresenter(this, interactor, applicationContext) }
    private var modelSubMenu: SubMenu? = null
    private var generatorLoader: GeneratorLoader = GeneratorLoader()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(toolbar)
        setupDrawer(toolbar)

        presenter.addModelsToNavBar()
        generatorLoader.load(assets)
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
            val fragment: ModelFragment = ModelFragment.newInstance(modelUrl, "")
            fragment.displayFocusedImage(generatorLoader.sample())
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .disallowAddToBackStack()
                    .commit()

    }

    override fun modeToNavBar(generator: Generator, index: Int) {
        modelSubMenu?.add(R.id.group1, index, index, generator.name)
        modelSubMenu?.getItem(index)?.setIcon(R.drawable.ic_lock)

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



}
