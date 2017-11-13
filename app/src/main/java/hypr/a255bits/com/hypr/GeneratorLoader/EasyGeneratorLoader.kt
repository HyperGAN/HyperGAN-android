package hypr.a255bits.com.hypr.GeneratorLoader

import android.graphics.Bitmap
import hypr.a255bits.com.hypr.Generator.Generator

class EasyGeneratorLoader(val gen: Generator): GeneratorLoader(gen.generator!!){
    var baseImage: Bitmap? = null
    var encoded: FloatArray? = null
    var mask: FloatArray? = null
    var direction: FloatArray? = null

    fun sampleImageWithImage(image: Bitmap?): IntArray {
        direction = this.random_z()
        val scaled = Bitmap.createScaledBitmap(image, gen.generator?.output?.width!!, gen.generator?.output?.height!!, false)

        baseImage = scaled
        encoded = this.encode(scaled)

        mask = this.mask(scaled)
        return this.sample(encoded!!, 0.0f, mask, direction!!, scaled)
    }
    fun sampleImageWithoutImage(): IntArray {
        val scaled = Bitmap.createBitmap(gen.generator?.output?.width!!, gen.generator?.output?.height!!, Bitmap.Config.ARGB_8888)
        mask = this.mask(scaled)
        val direction = this.random_z()
        baseImage = scaled
        encoded = this.encode(scaled)
        return this.sampleRandom(encoded!!, 0.0f, direction, mask!!, scaled)
    }
    fun sampleImageWithZValue(slider: Float): IntArray{
        val direction = this.direction ?: this.random_z()
        return this.sample(this.encoded!!, slider, mask, direction, baseImage!!)
    }
}