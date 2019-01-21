package hypr.a255bits.com.hypr.GeneratorLoader

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import hypr.a255bits.com.hypr.Generator.Generator
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.experimental.GpuDelegate;
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer

open class GeneratorLoader {
    lateinit var inference: Interpreter
    val PB_FILE_PATH: String = "generators/configurable-256x256-2.tflite" // TODO generator['model_url']

    var generator: Generator? = null
    var channels: Int = 0
    var width: Int = 0
    var height: Int = 0
    var z_dimsArray: LongArray = longArrayOf()
    var z_dims: Long = 0
    var raw: FloatArray = floatArrayOf()
    var index: Int? = 0

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
        val asset = assets.open(PB_FILE_PATH)
        val bytes:ByteArray = asset.readBytes()
        val buffer = ByteBuffer.allocateDirect(bytes.size)
        buffer.put(bytes)
        //val delegate = GpuDelegate();
        //val options = Interpreter.Options().addDelegate(delegate)

        this.inference = Interpreter( buffer )
        //ByteBuffer.wrap(FileInputStream(PB_FILE_PATH).readBytes()) )

        //System.loadLibrary("tensorflow_inference")
    }

    fun load(assets: AssetManager, file: File) {
        this.inference = Interpreter(file)

//        this.inference = TensorFlowInferenceInterface(assets, file.absolutePath)

    }

    fun sample(z: FloatArray, slider: Float, mask: FloatArray, direction: FloatArray, bitmap: Bitmap): IntArray {
        print("Sampling ")
        feedInput(bitmap)

        var inputs:Array<Any> = arrayOf(z,z,slider)
        var outputs:HashMap<Int, Any> = hashMapOf(0 to this.raw)
        this.inference.runForMultipleInputsOutputs(inputs, outputs)


        return manipulatePixelsInBitmap()
    }

    fun mask(bitmap: Bitmap): FloatArray {
        feedInput(bitmap)
        val floatValues = FloatArray(width * height)

        //if (index == 0) {

        //    this.inference.run(arrayOf("Tanh_4"))

        //    this.inference.fetch("Tanh_4", floatValues)
        //}

        return floatValues
    }

    fun sampleRandom(z: FloatArray, slider: Float, direction: FloatArray, mask: FloatArray, scaled: Bitmap): IntArray {
        feedInput(scaled)
        mask.forEachIndexed { index, item ->
            mask[index] = 0.0f
        }

        //this.inference.feed("concat", z, *z_dimsArray)
        //this.inference.feed("direction", direction, *z_dimsArray)

        /*val maskDims = longArrayOf(1, width.toLong(), height.toLong(), 1)
        if (index == 0) {

            this.inference.feed("Tanh_1", mask, *maskDims)
        }*/

        val dims = longArrayOf(1.toLong(), 1.toLong(), 1.toLong(), 1.toLong())
        //this.inference.feed("slider", floatArrayOf(slider), *dims)

        //this.inference.run("Tanh"))
        //inference.readNodeFloat(OUTPUT_NODE, resu)

        //inference.run(..)
        //this.inference.fetch("Tanh", this.raw)

        var inputs:Array<Any> = arrayOf(z)
        var outputs:HashMap<Int, Any> = hashMapOf(0 to this.raw)
        this.inference.runForMultipleInputsOutputs(inputs, outputs)

        return manipulatePixelsInBitmap()
    }

    fun get_z(z: FloatArray, slider: Float, direction: FloatArray): FloatArray {
        val floatValues = FloatArray(z_dims.toInt())

        //this.inference.feed("concat", z, *z_dimsArray)

        val dims = longArrayOf(1.toLong(), 1.toLong())
        //this.inference.feed("slider", floatArrayOf(slider), *dims)

        //this.inference.feed("direction", direction, *z_dimsArray)

        //this.inference.run(arrayOf("add"))

        //this.inference.fetch("add", floatValues)

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
        if (index == 0) {

            //this.inference.feed("input", floatValues, *dims)
        }
    }

    fun encode(bitmap: Bitmap): FloatArray {
        feedInput(bitmap)

        //if (index == 0) {
        //    this.inference.run(arrayOf("Tanh"))
        //}

        val z = FloatArray(z_dims.toInt())

        //if (index == 0) {

        //    this.inference.fetch("Tanh", z)
        //}

        return z
    }

    fun random_z(): FloatArray {
        //this.inference.run(arrayOf("random_z"))
        val z = FloatArray(z_dims.toInt())
        //this.inference.fetch("random_z", z)

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