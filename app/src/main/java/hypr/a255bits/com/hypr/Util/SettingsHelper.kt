package hypr.a255bits.com.hypr.Util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SettingsHelper(context: Context) {
    val FIRST_TIME_OPENED = "firstTimeOpened"
    val preference: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val editor: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun isFirstTimeOpenedApp(): Boolean{
        return preference.getBoolean(FIRST_TIME_OPENED, true)
    }

    fun setAppOpenedAlready(){
        editor.edit().putBoolean(FIRST_TIME_OPENED, false).apply()
    }
}