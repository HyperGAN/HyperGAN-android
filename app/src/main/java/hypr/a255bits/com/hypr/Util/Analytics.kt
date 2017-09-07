package hypr.a255bits.com.hypr.Util

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Created by tedho on 9/7/2017.
 */

class Analytics(val context: Context){
    val analytics = FirebaseAnalytics.getInstance(context)

    fun logGeneratorDownload(){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "generator")
        analytics.logEvent("generator", bundle)
    }
}