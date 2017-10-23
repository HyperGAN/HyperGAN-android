package hypr.a255bits.com.hypr.ModelFragmnt

import android.graphics.Bitmap
import android.graphics.Matrix


/**
 * Created by tedho on 10/23/2017.
 */

class InlineImage{
    var widthRatio: Int = 0
    var heightRatio: Int = 0

    fun setBeforeAfterCropSizingRatio(oldCroppedImage: Bitmap, newCroppedImage: Bitmap){
        widthRatio = oldCroppedImage.width / newCroppedImage.width
        heightRatio = oldCroppedImage.height / newCroppedImage.height
    }
    fun inlineCroppedImageToFullImage(croppedImage: Bitmap, fullImage: Bitmap){
        val newWidth = croppedImage.width / widthRatio
        val newHeight = croppedImage.height / heightRatio
        val scaledBitmapToCroppedImage: Bitmap = scaleBitmap(fullImage, newWidth, newHeight)
    }

    private fun scaleBitmap(fullImage: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = fullImage.width
        val height = fullImage.height
        val scaleWidth = newWidth as Float / width
        val scaleHeight = newHeight as Float / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(
                fullImage, 0, 0, width, height, matrix, false)
        fullImage.recycle()
        return resizedBitmap
    }
}