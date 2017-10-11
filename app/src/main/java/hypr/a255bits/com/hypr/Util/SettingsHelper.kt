package hypr.a255bits.com.hypr.Util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SettingsHelper(context: Context) {
    val FIRST_TIME_OPENED = "firstTimeOpened"
    val RESTORE_MODEL_IMAGE = "restoreImage"
    val preference: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val editor: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun isFirstTimeOpenedApp(): Boolean {
        return preference.getBoolean(FIRST_TIME_OPENED, true)
    }

    fun setAppOpenedAlready() {
        editor.edit().putBoolean(FIRST_TIME_OPENED, false).apply()
    }

    fun setModelImagePath(filePath: String) {
        editor.edit().putString(RESTORE_MODEL_IMAGE, filePath).apply()
    }

    fun isModelImageRestoreable(): Boolean {
        return !preference.getString(RESTORE_MODEL_IMAGE, "").isEmpty()
    }

    fun getModelImagePath(): String {
        return preference.getString(RESTORE_MODEL_IMAGE, "")
    }
}