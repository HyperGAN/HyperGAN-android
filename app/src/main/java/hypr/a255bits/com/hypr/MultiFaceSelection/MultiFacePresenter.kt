package hypr.a255bits.com.hypr.MultiFaceSelection

import android.graphics.Bitmap
import android.graphics.PointF
import android.os.Bundle
import com.pawegio.kandroid.getParcelableMutableList
import hypr.a255bits.com.hypr.Util.toBitmap

/**
 * Created by tedho on 10/13/2017.
 */

class MultiFacePresenter: MultiFaceMVP.presenter{
    lateinit var imageOfPeople: Bitmap
    lateinit var faceLocationsInImage: MutableList<PointF>

    override fun getInfoFromExtras(extras: Bundle) {
        val imageOfPeople = extras.getByteArray("image")
        this.imageOfPeople = imageOfPeople.toBitmap()!!
        faceLocationsInImage = extras.getParcelableMutableList("faceLocations")
    }

}