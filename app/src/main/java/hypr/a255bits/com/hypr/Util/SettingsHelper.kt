package hypr.a255bits.com.hypr.Util

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Rect
import android.preference.PreferenceManager

class SettingsHelper(val context: Context) {
    val FIRST_TIME_OPENED = "firstTimeOpened"
    val RESTORE_MODEL_IMAGE = "restoreImage"
    val RESTORE_MODEL_FULL_IMAGE = "restoreFullImage"
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
    fun setFullImagePath(filePath: String){
        editor.edit().putString(RESTORE_MODEL_FULL_IMAGE, filePath).apply()
    }

    fun isModelImageRestoreable(): Boolean {
        return !preference.getString(RESTORE_MODEL_IMAGE, "").isEmpty()
    }

    fun getModelImagePath(): String {
        return preference.getString(RESTORE_MODEL_IMAGE, "")
    }

    fun getFullImagePath(): String{
       return preference.getString(RESTORE_MODEL_FULL_IMAGE, "")
    }

    fun saveFaceLocation(face: Rect) {
            val edit = editor.edit()
            edit.putFloat("face_x", face.centerX().toFloat())
            edit.putFloat("face_y", face.centerY().toFloat())
            edit.putFloat("face_width", face.width().toFloat())
            edit.putFloat("face_height", face.height().toFloat())
            edit.apply()
    }

    fun getFaceLocation(): Rect {
        val x = preference.getFloat("face_x", 0.0f)
        val y = preference.getFloat("face_y", 0.0f)
        val width = preference.getFloat("face_width", 0.0f)
        val height = preference.getFloat("face_height", 0.0f)
        return FaceDetection(context).faceToRect(x, y, width, height)
    }
}