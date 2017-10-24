package hypr.a255bits.com.hypr.ModelFragmnt

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint


/**
 * Created by tedho on 10/23/2017.
 */

class InlineImage{
    var widthRatio: Float = 0.0f
    var heightRatio: Float = 0.0f
    var isOldCoppedImageBigger = false

    fun setBeforeAfterCropSizingRatio(oldCroppedImage: Bitmap, newCroppedImage: Bitmap){
        if(oldCroppedImage.byteCount < newCroppedImage.byteCount){
            isOldCoppedImageBigger = true
            widthRatio = newCroppedImage.width.toFloat() / oldCroppedImage.width.toFloat()
            heightRatio = newCroppedImage.height.toFloat() / oldCroppedImage.height.toFloat()

        }else{
            widthRatio = oldCroppedImage.width.toFloat() / newCroppedImage.width.toFloat()
            heightRatio = oldCroppedImage.height.toFloat() / newCroppedImage.height.toFloat()
        }
    }
    fun inlineCroppedImageToFullImage(croppedImage: Bitmap, fullImage: Bitmap){
        val newWidth: Float
        val newHeight: Float
        if(isOldCoppedImageBigger){
            newWidth = croppedImage.width * widthRatio
            newHeight = croppedImage.height * heightRatio
        }else{
            newWidth = croppedImage.width / widthRatio
            newHeight = croppedImage.height / heightRatio

        }
        val scaledBitmapToCroppedImage: Bitmap = scaleBitmap(fullImage, newWidth.toInt(), newHeight.toInt())
        val fullImageWithCroppedImageInline: Bitmap = insertCroppedImageWithinFullImage(scaledBitmapToCroppedImage, croppedImage)
        println("hi")
    }

    private fun insertCroppedImageWithinFullImage(scaledBitmapToCroppedImage: Bitmap, croppedImage: Bitmap): Bitmap {
        val fullImageCanvas = Canvas(scaledBitmapToCroppedImage)
        val left: Float = (fullImageCanvas.width - croppedImage.width).toFloat()
        fullImageCanvas.drawBitmap(croppedImage, left, 0.0f, Paint())
        return scaledBitmapToCroppedImage
    }

    private fun scaleBitmap(fullImage: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val width = fullImage.width
        val height = fullImage.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
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