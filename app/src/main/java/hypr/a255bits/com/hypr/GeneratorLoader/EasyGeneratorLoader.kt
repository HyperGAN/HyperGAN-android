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
        featureEnabled(Feature.MASK)
    }
    var z1: FloatArray = this.random_z()
    var z2: FloatArray = this.random_z()
    val inliner = InlineImage()

    fun sampleImageWithImage(person: Person, image: Bitmap?, croppedPoint: Rect): Bitmap? {
        val scaled = Bitmap.createScaledBitmap(image, generator?.generator?.output?.width!!, generator?.generator?.output?.height!!, false)
        baseImage = scaled
        mask = this.mask(scaled)
        val image = if (featureEnabled(Feature.ENCODING)) {
            encoded = this.encode(scaled)
            this.sample(encoded!!, mask, scaled).toBitmap(this.width, this.height)
        } else {
            z1 = this.random_z()
            z2 = this.random_z()
            this.sample(this.get_z(0.0f), mask, scaled).toBitmap(this.width, this.height)
        }
        return inlineImage(person, image, croppedPoint)
    }

    fun get_z(slider: Float): FloatArray {
        val z = FloatArray(z_dims.toInt())
        val s:Float = (slider + 1.0f) / 2.0f
        for (i in 0..z_dims.toInt() - 1)
            z[i] = slider * z1!![i] + (1.0f-slider) * z2!![i]
        return z
    }

    fun sampleImageWithoutImage(): IntArray {
        val scaled = Bitmap.createBitmap(generator?.generator?.output?.width!!, generator?.generator?.output?.height!!, Bitmap.Config.ARGB_8888)
        mask = this.mask(scaled)
        baseImage = scaled
        val sample = if(featureEnabled(Feature.ENCODING)){
            encoded = this.encode(scaled)
            this.sampleRandom(encoded!!, mask, scaled)

        }else{
            z1 = this.random_z()
            z2 = this.random_z()
            this.sampleRandom(z1!!, mask, scaled)
        }
        return sample
    }

    fun sampleImageWithZValue(slider: Float): IntArray {
        this.encoded = this.random_z()
        return this.sample(this.get_z(slider), mask, baseImage!!)
    }

    fun inlineImage(person: Person, newCroppedImage: Bitmap, croppedPoint: Rect): Bitmap? {
        val image: Bitmap?
        if (featureEnabled(Feature.INLINE)) {
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