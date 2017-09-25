package hypr.a255bits.com.hypr.Util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class ImageSaver {

    fun saveImageToInternalStorage(focusedImageBitmap: Bitmap?, context: Context, folderName: String = "hyprimages"): Boolean {

            val root = Environment.getExternalStoragePublicDirectory(folderName).toString()
            val myDir = File(root)
            if (!myDir.exists()) {
                myDir.mkdir()
            }
            val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSSSS")
            val now = Date()
            val fileName = formatter.format(now)

            val fname = "/Image_$fileName.jpg"
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

    fun saveImageToFile(file: File, image: ByteArray?) {
        val fos = FileOutputStream(file)
        fos.write(image)
    }

    fun writeByteArrayToFile(fileLocation: String, imageInBytes: ByteArray?) {
        val file = File(fileLocation)
        file.createNewFile()
        val fileOutput = FileOutputStream(file)
        fileOutput.write(imageInBytes)
    }
    private fun uriToBitmap(imageLocation: Uri, context: Context): Bitmap {
        return MediaStore.Images.Media.getBitmap(context.contentResolver, imageLocation)
    }
    fun uriToByteArray(imageLocation: Uri, context: Context): ByteArray? {
        val bitmap = uriToBitmap(imageLocation, context)
        return bitmapToByteArray(bitmap)
    }
    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()

    }

}
