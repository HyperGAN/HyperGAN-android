package hypr.a255bits.com.hypr.ModelFragmnt

import android.graphics.*


class InlineImage{
    var widthRatio: Float = 0.0f
    var heightRatio: Float = 0.0f
    var isOldCoppedImageBigger = false


    val paint = Paint()
    init{

        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
    }

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
        val fullImageWithCroppedImageInline: Bitmap = insertCroppedImageWithinFullImage(fullImage, croppedImage, croppedPoint)
        return fullImageWithCroppedImageInline
    }

    private fun insertCroppedImageWithinFullImage(fullImage: Bitmap, croppedImage: Bitmap, croppedPoint: Rect): Bitmap {
        val fullImageMutable = fullImage.copy(Bitmap.Config.ARGB_8888, true)
        val fullImageCanvas = Canvas(fullImageMutable)
        val scaledCropped = scaleBitmap(croppedImage, croppedPoint.width(), croppedPoint.height())
        fullImageCanvas.drawBitmap(scaledCropped, croppedPoint.left.toFloat(), croppedPoint.top.toFloat(), Paint())
        return fullImageMutable
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