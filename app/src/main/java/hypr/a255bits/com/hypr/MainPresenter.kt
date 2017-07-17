package hypr.a255bits.com.hypr

import android.content.Context

/**
 * Created by ted on 7/17/17.
 */

class MainPresenter(val applicationContext: Context, val view: MainMvp.view) : MainMvp.presenter{

    override fun displayGallery() {
        view.displayGallery()
    }

}