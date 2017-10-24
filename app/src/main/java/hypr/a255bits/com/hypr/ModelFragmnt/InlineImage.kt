package hypr.a255bits.com.hypr.ModelFragmnt

import android.graphics.*


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
    fun inlineCroppedImageToFullImage(croppedImage: Bitmap, fullImage: Bitmap, croppedPoint: Rect): Bitmap {
        val newWidth: Float
        val newHeight: Float
        if(isOldCoppedImageBigger){
            newWidth = fullImage.width * widthRatio
            newHeight = fullImage.height * heightRatio
        }else{
            newWidth = fullImage.width / widthRatio
            newHeight = fullImage.height / heightRatio

        }
        val offsetY: Int  = (croppedPoint.centerY() + (newHeight - fullImage.height)).toInt()
        croppedPoint.offset(0, offsetY)
        val scaledBitmapToCroppedImage: Bitmap = scaleBitmap(fullImage, newWidth.toInt(), newHeight.toInt())
        val fullImageWithCroppedImageInline: Bitmap = insertCroppedImageWithinFullImage(scaledBitmapToCroppedImage, croppedImage, croppedPoint)
        println("hi")
        return fullImageWithCroppedImageInline
    }

    private fun insertCroppedImageWithinFullImage(scaledBitmapToCroppedImage: Bitmap, croppedImage: Bitmap, croppedPoint: Rect): Bitmap {
        val fullImageCanvas = Canvas(scaledBitmapToCroppedImage)
        val left: Float = (fullImageCanvas.width - croppedImage.width).toFloat()
        fullImageCanvas.drawBitmap(croppedImage, croppedPoint.left.toFloat(), croppedPoint.top.toFloat(), Paint())
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