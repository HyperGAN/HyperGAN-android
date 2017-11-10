package hypr.a255bits.com.hypr.GeneratorLoader

import android.graphics.Bitmap
import hypr.a255bits.com.hypr.Generator.Generator_

class EasyGeneratorLoader(gen: Generator_): GeneratorLoader(gen){
    var baseImage: Bitmap? = null
    var encoded: FloatArray? = null
    var mask: FloatArray? = null

    fun sampleImageWithImage(image: Bitmap?): IntArray {
        val direction = this.random_z()
        val scaled = Bitmap.createScaledBitmap(image, this.width, this.height, false)
        //TODO val scaled = Bitmap.createScaledBitmap(image, generator.output.width, generator.output.height, false)

        baseImage = scaled
        encoded = this.encode(scaled)

        mask = this.mask(scaled)
        return this.sample(encoded!!, 0.0f, mask, direction, scaled)
    }
    fun sampleImageWithoutImage(): IntArray {
        val scaled = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        //TODO val scaled = Bitmap.createBitmap(generator.output.width, generator.output.height, Bitmap.Config.ARGB_8888)
        mask = this.mask(scaled)
        val direction = this.random_z()
        baseImage = scaled
        encoded = this.encode(scaled)
        return this.sampleRandom(encoded!!, 0.0f, direction, mask!!, scaled)
    }
}