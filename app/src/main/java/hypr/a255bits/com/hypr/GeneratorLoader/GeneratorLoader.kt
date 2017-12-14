package hypr.a255bits.com.hypr.GeneratorLoader

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import hypr.a255bits.com.hypr.Generator.Generator
import org.tensorflow.contrib.android.TensorFlowInferenceInterface
import java.io.File

open class GeneratorLoader() {
    lateinit var inference: TensorFlowInferenceInterface
    val PB_FILE_PATH: String = "file:///android_asset/generators/expression-model.pb" // TODO generator['model_url']

    var generator: Generator? = null
    var channels: Int = 0
    var width: Int = 0
    var height: Int = 0
    var z_dimsArray: LongArray = longArrayOf()
    var z_dims: Long = 0
    var raw: FloatArray = floatArrayOf()
    var index: Int? = 0
    var direction: FloatArray? = null


    fun setIndex(index: Int) {
        this.index = index
    }

    fun loadGenerator(generator: Generator) {
        this.generator = generator
        this.width = generator.generator?.input?.width!!
        this.height = generator.generator!!.input?.height!!
        z_dimsArray = generator.generator!!.input!!.z_dims!!.map { item -> item.toLong() }.toLongArray()
        z_dims = z_dimsArray.fold(1.toLong(), { mul, next -> mul * next })
        channels = generator.generator!!.input!!.channels
        raw = FloatArray(width * height * channels)

    }

    fun load(assets: AssetManager) {
        System.loadLibrary("tensorflow_inference")
        this.inference = TensorFlowInferenceInterface(assets, PB_FILE_PATH)
    }

    fun load(assets: AssetManager, file: File) {
        load(assets)
    }

    fun sample(z: FloatArray, slider: Float, mask: FloatArray, direction: FloatArray, bitmap: Bitmap): IntArray {
        print("Sampling ")
        feedInput(bitmap)


        this.inference.feed("concat", z, *z_dimsArray)
        this.inference.feed("direction", direction, *z_dimsArray)
        Log.i("slider", "SLIDER VALUE " + slider)

        val maskDims = longArrayOf(1, width.toLong(), height.toLong(), 1)
        //if (mask.isNotEmpty()) {
        //    this.inference.feed("Tanh_1", mask, *maskDims)
        //}
        val dims = longArrayOf(1.toLong(), 1.toLong())
        this.inference.feed("slider", floatArrayOf(slider), *dims)
        this.inference.run(arrayOf("add_27"))
        //inference.readNodeFloat(OUTPUT_NODE, resu)

        //inference.run(..)
        this.inference.fetch("add_27", this.raw)

        return manipulatePixelsInBitmap()
    }

    fun sampleTensor(name: String, z: FloatArray, slider: Float, mask: FloatArray, bitmap: Bitmap): IntArray {
        this.direction = this.direction ?: random_z()
        print("Sampling ")
        feedInput(bitmap)


        this.inference.feed("concat", z, *z_dimsArray)
        this.inference.feed("direction", this.direction, *z_dimsArray)
        Log.i("slider", "SLIDER VALUE " + slider)

        val maskDims = longArrayOf(1, width.toLong(), height.toLong(), 1)
        //if (mask.isNotEmpty()) {
        //    this.inference.feed("Tanh_1", mask, *maskDims)
        //}
        val dims = longArrayOf(1.toLong(), 1.toLong())
        this.inference.feed("slider", floatArrayOf(slider), *dims)
        this.inference.run(arrayOf(name))
        //inference.readNodeFloat(OUTPUT_NODE, resu)

        //inference.run(..)
        this.inference.fetch(name, this.raw)

        return manipulatePixelsInBitmap()
    }

    fun mask(bitmap: Bitmap): FloatArray {
        feedInput(bitmap)
        val floatValues = FloatArray(width * height * 9)

        this.inference.run(arrayOf("Maximum_5"))
        this.inference.fetch("Maximum_5", floatValues)

        return floatValues
    }

    fun sampleRandom(z: FloatArray, slider: Float, mask: FloatArray, scaled: Bitmap): IntArray {
        this.direction = this.direction ?: random_z()
        feedInput(scaled)
        //mask.forEachIndexed { index, item ->
        //    mask[index] = 0.0f
        //}

        this.inference.feed("concat", z, *z_dimsArray)
        this.inference.feed("direction", direction, *z_dimsArray)

        val maskDims = longArrayOf(1, width.toLong(), height.toLong(), 1)
        //if (index == 0) {

        //    this.inference.feed("Tanh_1", mask, *maskDims)
        //}

        val dims = longArrayOf(1.toLong(), 1.toLong())
        this.inference.feed("slider", floatArrayOf(slider), *dims)
        this.inference.run(arrayOf("add_27"))
        //inference.readNodeFloat(OUTPUT_NODE, resu)

        //inference.run(..)
        this.inference.fetch("add_27", this.raw)

        val image = manipulatePixelsInBitmap()
        return image
    }

    fun get_z(z: FloatArray, slider: Float, direction: FloatArray): FloatArray {
        val floatValues = FloatArray(z_dims.toInt())

        this.inference.feed("concat", z, *z_dimsArray)

        val dims = longArrayOf(1.toLong(), 1.toLong())
        this.inference.feed("slider", floatArrayOf(slider), *dims)

        this.inference.feed("direction", direction, *z_dimsArray)

        this.inference.run(arrayOf("add"))

        this.inference.fetch("add", floatValues)

        return floatValues
    }

    fun feedInput(bitmap: Bitmap) {
        val intValues = IntArray(width * height)
        val floatValues = FloatArray(width * height * channels)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        for (i in 0..intValues.size - 1) {
            val ival = intValues[i]
            floatValues[i * 3] = ((ival shr 16 and 0xFF) / 255.0f - 0.5f) * 2
            floatValues[i * 3 + 1] = ((ival shr 8 and 0xFF) / 255.0f - 0.5f) * 2
            floatValues[i * 3 + 2] = ((ival and 0xFF) / 255.0f - 0.5f) * 2
        }
        val dims = longArrayOf(1.toLong(), width.toLong(), height.toLong(), channels.toLong())
        this.inference.feed("shuffle_batch", floatValues, *dims)
    }

    fun encode(bitmap: Bitmap): FloatArray {
        return random_z()
        feedInput(bitmap)

        this.inference.run(arrayOf("Maximum"))

        val z = FloatArray(z_dims.toInt())

        this.inference.fetch("Maximum", z)

        return z
    }

    fun random_z(): FloatArray {
        this.inference.run(arrayOf("random_z"))
        val z = FloatArray(z_dims.toInt())
        this.inference.fetch("random_z", z)

        return z
    }


    private fun manipulatePixelsInBitmap(): IntArray {
        val pixelsInBitmap = IntArray(width * height)
        for (i in 0..pixelsInBitmap.size - 1) {

            val raw_one: Int = (((raw[i * 3] + 1) / 2.0 * 255).toInt()) shl 16
            val raw_two: Int = (((raw[i * 3 + 1] + 1) / 2.0 * 255).toInt()) shl 8
            val raw_three: Int = ((raw[i * 3 + 2] + 1) / 2.0 * 255).toInt()
            pixelsInBitmap[i] = 0xFF000000.toInt() or raw_one or raw_two or raw_three
        }
        return pixelsInBitmap
    }

}