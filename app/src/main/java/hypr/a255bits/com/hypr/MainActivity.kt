package hypr.a255bits.com.hypr

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity(), MainMvp.view{
    val presenter by lazy{MainPresenter(applicationContext, this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun chooseImageClick(view: View){
        presenter.displayGallery()

    }

    override fun displayGallery() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
