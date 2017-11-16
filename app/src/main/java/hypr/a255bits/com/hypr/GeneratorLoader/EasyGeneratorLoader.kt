package hypr.a255bits.com.hypr.GeneratorLoader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.ModelFragmnt.InlineImage
import hypr.a255bits.com.hypr.Util.toBitmap
import kotlin.properties.Delegates

class EasyGeneratorLoader(val gen: Generator, val context: Context): GeneratorLoader(gen.generator!!) {
    var baseImage: Bitmap? = null
    var encoded: FloatArray? = null
    var mask: FloatArray by Delegates.vetoable(floatArrayOf()) { property, oldValue, newValue ->
        featureEnabled("mask")
    }
    var direction: FloatArray? = null
    val inliner = InlineImage()

    init{
        this.load(context.assets)
    }
    fun sampleImageWithImage(person: Person, image: Bitmap?, croppedPoint: Rect): Bitmap? {
        direction = this.random_z()
        val scaled = Bitmap.createScaledBitmap(image, gen.generator?.output?.width!!, gen.generator?.output?.height!!, false)

        baseImage = scaled
        encoded = this.encode(scaled)

        mask = this.mask(scaled)
        val image = this.sample(encoded!!, 0.0f, mask, direction!!, scaled).toBitmap(this.width, this.height)
        return inlineImage(person, image, croppedPoint)
    }

    fun sampleImageWithoutImage(): IntArray {
        val scaled = Bitmap.createBitmap(gen.generator?.output?.width!!, gen.generator?.output?.height!!, Bitmap.Config.ARGB_8888)
        mask = this.mask(scaled)
        val direction = this.random_z()
        baseImage = scaled
        encoded = this.encode(scaled)
        return this.sampleRandom(encoded!!, 0.0f, direction, mask, scaled)
    }

    fun sampleImageWithZValue(slider: Float): IntArray {
        val direction = this.direction ?: this.random_z()
        return this.sample(this.encoded!!, slider, mask, direction, baseImage!!)
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
        return gen.features.contains(feature)
    }
}