package hypr.a255bits.com.hypr.Util

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Rect
import android.preference.PreferenceManager
import com.google.android.gms.vision.face.Face

class SettingsHelper(val context: Context) {
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

    fun saveFaceLocation(face: Face?) {
        if (face != null) {
            val edit = editor.edit()
            edit.putFloat("face_x", face.position.x)
            edit.putFloat("face_y", face.position.y)
            edit.putFloat("face_width", face.width)
            edit.putFloat("face_height", face.height)
            edit.apply()
        }
    }

    fun getFaceLocation(): Rect {
        val x = preference.getFloat("face_x", 0.0f)
        val y = preference.getFloat("face_y", 0.0f)
        val width = preference.getFloat("face_width", 0.0f)
        val height = preference.getFloat("face_height", 0.0f)
        return FaceDetection(context).faceToRect(x, y, width, height)
    }
}