package hypr.a255bits.com.hypr

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore

/**
 * Created by ted on 7/17/17.
 */
class MainInteractor(val context: Context): MainMvp.interactor{
    override fun uriToBitmap(imageLocation: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(context.contentResolver, imageLocation);

    }


}
