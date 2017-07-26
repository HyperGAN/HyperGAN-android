package hypr.a255bits.com.hypr.Util

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.provider.MediaStore.Images
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*


class ImageSaver {

    fun saveImageToInternalStorage(focusedImageBitmap: Bitmap?, context: Context): Boolean {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File(root)
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
        val now = Date()
        val fileName = formatter.format(now)

        val fname = "/Image-$fileName.jpg"
        val file = File(myDir, fname)
        try {
            val out = FileOutputStream(file)
            focusedImageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file))
        context.sendBroadcast(Intent(intent))
        return true

    }
}
