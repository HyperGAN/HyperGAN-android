package hypr.a255bits.com.hypr.Network

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.doAsync
import java.io.File


class ModelDownloader(val storageReference: StorageReference) {

    fun getFile(file: File, storageName: String): FileDownloadTask {
        val fileName = storageReference.child(storageName)
        return fileName.getFile(file)
    }
}