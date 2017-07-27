package hypr.a255bits.com.hypr.Network

import android.net.Uri
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.doAsync
import java.io.File


class ModelDownloader(val storageReference: StorageReference) {

    fun getFile(file: File): FileDownloadTask.TaskSnapshot? {
//        val  isSuccessful = storageReference.putFile(Uri.fromFile(file)).result
        val uploadTask = storageReference.putBytes(byteArrayOf(1,2,3)).result
        return storageReference.getFile(file).result
    }
}