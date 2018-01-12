package hypr.a255bits.com.hypr.GeneratorLoader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.ModelFragmnt.InlineImage
import hypr.a255bits.com.hypr.Util.toBitmap
import kotlin.properties.Delegates

class EasyGeneratorLoader(var gen: Generator) : GeneratorLoader() {
    var baseImage: Bitmap? = null
    var encoded: FloatArray? = null
    var mask: FloatArray by Delegates.vetoable(floatArrayOf()) { property, oldValue, newValue ->
        featureEnabled("mask")
    }
    val inliner = InlineImage()

    fun loadAssets(context: Context) {
        this.load(context.assets)
    }

    fun sampleImageWithImage(person: Person, image: Bitmap?, croppedPoint: Rect): Bitmap? {
        val scaled = Bitmap.createScaledBitmap(image, generator?.generator?.output?.width!!, generator?.generator?.output?.height!!, false)
        baseImage = scaled
        mask = this.mask(scaled)
        val image = if (featureEnabled("encoding")) {
            encoded = this.encode(scaled)
            this.encoded = encoded
            this.sampleTensor("add_90",encoded!!, 0.0f, mask, scaled).toBitmap(this.width, this.height)
        } else {
            this.sampleTensor("add_90",direction!!,0.0f, mask, scaled).toBitmap(this.width, this.height)
        }
        return inlineImage(person, image, croppedPoint)
    }

    fun sampleImageWithoutImage(): IntArray {
        val scaled = Bitmap.createBitmap(generator?.generator?.output?.width!!, generator?.generator?.output?.height!!, Bitmap.Config.ARGB_8888)
        //mask = this.mask(scaled)
        baseImage = scaled
        val sample = if(featureEnabled("encoding")){
            encoded = this.encode(scaled)
            this.sampleRandom(encoded!!, 0.0f, mask!!, scaled!!)

        }else{
            this.sampleRandom(direction!!, 0.0f, mask!!, scaled!!)
        }
        return sample
    }

    fun sampleImageWithZValue(slider: Float): IntArray {
        return this.sampleTensor( "add_90", this.encoded!!, slider, mask, baseImage!!)
    }

    fun inlineImage(person: Person, newCroppedImage: Bitmap, croppedPoint: Rect): Bitmap? {
        val image: Bitmap?
        if (featureEnabled("inline")) {
            val fullImage = person.fullImage
            val faceImage = person.faceImage?.toBitmap()
            image = if (faceImage != null) {
                inliner.setBeforeAfterCropSizingRatio(faceImage, newCroppedImage)
                fullImage.toBitmap()?.let { inliner.inlineCroppedImageToFullImage(newCroppedImage, it, croppedPoint) }
            } else {
                newCroppedImage
            }
        } else {
            image = newCroppedImage
        }
        return image
    }

    private fun featureEnabled(feature: String): Boolean {
        return generator!!.features.contains(feature)
    }
}
