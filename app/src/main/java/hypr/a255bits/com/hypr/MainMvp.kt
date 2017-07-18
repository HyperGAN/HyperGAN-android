package hypr.a255bits.com.hypr

/**
 * Created by ted on 7/17/17.
 */
interface MainMvp {
    interface view{
        fun displayGallery()

    }
    interface presenter{

        fun displayGallery()
    }

}
