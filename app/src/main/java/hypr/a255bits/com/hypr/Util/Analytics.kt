package hypr.a255bits.com.hypr.Util

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class Analytics(val context: Context){
    val analytics = FirebaseAnalytics.getInstance(context)

    fun logEvent(event: AnalyticsEvent){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, event.name)
        analytics.logEvent(event.name, bundle)
    }
}