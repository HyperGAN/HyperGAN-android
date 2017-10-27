package hypr.a255bits.com.hypr.ModelFragmnt

import android.graphics.*
import hypr.a255bits.com.hypr.Util.scaleBitmap


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
        val mutableFullImage = fullImage.copy(Bitmap.Config.ARGB_8888, true)
        val scaledCroppedImage = croppedImage.scaleBitmap(croppedPoint.width(), croppedPoint.height())
        insertCroppedImageWithinFullImage(mutableFullImage, scaledCroppedImage, croppedPoint)
        return mutableFullImage
    }

    private fun insertCroppedImageWithinFullImage(fullImageMutable: Bitmap, scaledCropped: Bitmap, croppedPoint: Rect): Bitmap {
        val fullImageCanvas = Canvas(fullImageMutable)
        fullImageCanvas.drawBitmap(scaledCropped, croppedPoint.left.toFloat(), croppedPoint.top.toFloat(), Paint())
        return fullImageMutable
    }


}