package hypr.a255bits.com.hypr.MultiFaceSelection

import android.graphics.Bitmap

class MultiFacePresenter(val view: MultiFaceMVP.view) : MultiFaceMVP.presenter{


    override fun displayImageWithFaces(imageOfPeoplesFaces: Bitmap?) {
        imageOfPeoplesFaces?.let { view.displayImageWithFaces(it) }
    }

}