package hypr.a255bits.com.hypr.Network

import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.StorageReference
import java.io.File


class ModelDownloader(val storageReference: StorageReference) {

    fun getFile(file: File, storageName: String): FileDownloadTask {
        val fileName = storageReference.child(storageName)
        return fileName.getFile(file)
    }
}