package hypr.gan.com.hypr.Network

import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File


class ModelDownloader {

    fun getFile(file: File, storageName: String): FileDownloadTask {
        val storageRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(storageName)
        return storageRef.getFile(file)
    }
}