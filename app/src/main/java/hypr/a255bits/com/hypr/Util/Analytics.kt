package hypr.a255bits.com.hypr.Util

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Created by tedho on 9/7/2017.
 */

class Analytics(val context: Context){
    val analytics = FirebaseAnalytics.getInstance(context)
    private val GENERATOR = "generator"
    private val EXISTING_PHOTO = "existingPhoto"
    private val TAKE_PICTURE = "takePicture"

    fun logGeneratorDownload(){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, GENERATOR)
        analytics.logEvent(GENERATOR, bundle)
    }

    fun pickExistingPhoto(){
       val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, EXISTING_PHOTO)
        analytics.logEvent(EXISTING_PHOTO, bundle)
    }

    fun pickTakePicture(){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, TAKE_PICTURE)
        analytics.logEvent(TAKE_PICTURE, bundle)

    }
}