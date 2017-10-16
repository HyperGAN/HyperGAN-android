package hypr.a255bits.com.hypr.MultiFaceSelection

import android.graphics.Bitmap

/**
 * Created by tedho on 10/13/2017.
 */

interface MultiFaceMVP{
    interface view{
        fun displayImageWithFaces(imageOfPeople: Bitmap)

    }
    interface presenter{
        fun displayImageWithFaces(image: Bitmap?)
    }
}