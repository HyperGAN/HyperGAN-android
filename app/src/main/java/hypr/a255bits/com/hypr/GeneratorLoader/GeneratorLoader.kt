package hypr.a255bits.com.hypr.GeneratorLoader

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import hypr.a255bits.com.hypr.Generator.Generator_
import org.tensorflow.contrib.android.TensorFlowInferenceInterface
import java.io.File

open class GeneratorLoader(val generator: Generator_) { //generator constructor parameter TODO
    lateinit var inference: TensorFlowInferenceInterface
    val PB_FILE_PATH: String = "file:///android_asset/generators/expression-model.pb" // TODO generator['model_url']

    var channels = generator.input!!.channels
    var width = generator.input!!.width
    var height = generator.input!!.height
    val z_dimsArray: LongArray = generator.input!!.z_dims!!.map { item -> item.toLong() }.toLongArray()
    var z_dims:Long = z_dimsArray.fold(1.toLong(), { mul, next -> mul * next })

    var raw: FloatArray = FloatArray(width * height * channels)

    fun load(assets: AssetManager) {
        System.loadLibrary("tensorflow_inference")
        this.inference = TensorFlowInferenceInterface(assets, PB_FILE_PATH)
    }

    fun load(assets: AssetManager, file: File){
        System.loadLibrary("tensorflow_inference")
        this.inference = TensorFlowInferenceInterface(assets, PB_FILE_PATH)
//        this.inference = TensorFlowInferenceInterface(assets, file.absolutePath)

    }

    fun sample(z:FloatArray, slider:Float, mask: FloatArray?, direction: FloatArray, bitmap:Bitmap): IntArray {
        print("Sampling ")
        feedInput(bitmap)


        this.inference.feed("concat", z, *z_dimsArray)
        this.inference.feed("direction", direction, *z_dimsArray)
        Log.i("slider", "SLIDER VALUE "+slider)

        val maskDims = longArrayOf(1, width.toLong(),height.toLong(), 1)
        this.inference.feed("Tanh_1", mask!!, *maskDims)

        val dims = longArrayOf(1.toLong(),1.toLong())
        this.inference.feed("slider", floatArrayOf(slider), *dims)
        this.inference.run(arrayOf("add_21"))
        //inference.readNodeFloat(OUTPUT_NODE, resu)

        //inference.run(..)
        this.inference.fetch("add_21", this.raw)

        return manipulatePixelsInBitmap()
    }

    fun mask(bitmap: Bitmap): FloatArray {
        feedInput(bitmap)
        val floatValues = FloatArray(width * height)

        this.inference.run(arrayOf("Tanh_4"))

        this.inference.fetch("Tanh_4", floatValues)

        return floatValues
    }

    fun sampleRandom(z: FloatArray, slider: Float, direction: FloatArray, mask: FloatArray, scaled: Bitmap): IntArray {
        feedInput(scaled)
        mask.forEachIndexed{ index, item->
            mask[index] = 0.0f
        }

        this.inference.feed("concat", z, *z_dimsArray)
        this.inference.feed("direction", direction, *z_dimsArray)

        val maskDims = longArrayOf(1, width.toLong(), height.toLong(), 1)
        this.inference.feed("Tanh_1", mask, *maskDims)

        val dims = longArrayOf(1.toLong(), 1.toLong())
        this.inference.feed("slider", floatArrayOf(slider), *dims)
        this.inference.run(arrayOf("add_21"))
        //inference.readNodeFloat(OUTPUT_NODE, resu)

        //inference.run(..)
        this.inference.fetch("add_21", this.raw)

        return manipulatePixelsInBitmap()
    }

    fun feedInput(bitmap: Bitmap) {
        val intValues = IntArray(width * height)
        val floatValues = FloatArray(width * height * channels)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        for (i in 0..intValues.size - 1) {
            val ival = intValues[i]
            floatValues[i * 3] = ((ival shr 16 and 0xFF) / 255.0f - 0.5f)*2
            floatValues[i * 3 + 1] = ((ival shr 8 and 0xFF) / 255.0f - 0.5f)*2
            floatValues[i * 3 + 2] = ((ival and 0xFF) / 255.0f - 0.5f)*2
        }
        val dims = longArrayOf(1.toLong(), width.toLong(), height.toLong(), channels.toLong())
        this.inference.feed("input", floatValues, *dims)
    }
    fun encode(bitmap: Bitmap): FloatArray {
        feedInput(bitmap)

        this.inference.run(arrayOf("Tanh"))

        val z = FloatArray(z_dims.toInt())

        this.inference.fetch("Tanh", z)

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

    fun manipulateBitmap(width: Int, height: Int, pixelsInBitmap: IntArray): Bitmap {
        val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixelsInBitmap, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        return bitmap
    }

}