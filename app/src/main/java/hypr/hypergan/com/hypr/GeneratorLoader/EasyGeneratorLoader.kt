package hypr.hypergan.com.hypr.GeneratorLoader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import hypr.hypergan.com.hypr.Generator.Generator
import hypr.hypergan.com.hypr.ModelFragmnt.InlineImage
import hypr.hypergan.com.hypr.Util.toBitmap
import kotlin.properties.Delegates

class EasyGeneratorLoader() : GeneratorLoader() {
    var baseImage: Bitmap? = null
    var encoded: FloatArray? = null
    var mask: FloatArray by Delegates.vetoable(floatArrayOf()) { property, oldValue, newValue ->
        featureEnabled(Feature.MASK)
    }
    var z1: FloatArray = this.random_z()
    var z2: FloatArray = this.random_z()
    val inliner = InlineImage()

    fun randomize() {
        if(generator!!.features.contains("halloween")) {
            z1 = this.random_z(5.0f, 10.0f)
            z2 = this.random_z(5.0f, 10.0f)
        } else {
            z1 = this.random_z()
            z2 = this.random_z()
        }
        if(generator!!.features.contains("style")) {
            z1 = this.style_z(z2)
        }

    }

    fun sampleImageWithImage(person: Person, image: Bitmap?, croppedPoint: Rect): Bitmap? {
        val scaled = Bitmap.createScaledBitmap(image, generator?.generator?.output?.width!!, generator?.generator?.output?.height!!, false)
        baseImage = scaled
        mask = this.mask(scaled)
        this.randomize()

        val image = this.sample(this.get_z(1.0f), mask, scaled).toBitmap(this.width, this.height)

        return inlineImage(person, image, croppedPoint)
    }

    fun get_z(slider: Float): FloatArray {
        val z = FloatArray(z_dims.toInt())
        val s:Float = (slider + 1.0f) / 2.0f
        for (i in 0..z_dims.toInt() - 1)
            z[i] = s * z1!![i] + (1.0f-s) * z2!![i]
        return z
    }

    fun sampleImageWithoutImage(): IntArray {
        val scaled = Bitmap.createBitmap(generator?.generator?.output?.width!!, generator?.generator?.output?.height!!, Bitmap.Config.ARGB_8888)
        mask = this.mask(scaled)
        baseImage = scaled
        this.randomize()
        return this.sample(this.get_z(0.5f), mask, scaled)
    }

    fun sampleWithSlider(slider: Float): IntArray {
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