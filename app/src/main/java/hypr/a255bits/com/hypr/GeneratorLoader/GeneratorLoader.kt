package hypr.a255bits.com.hypr.GeneratorLoader

import android.content.res.AssetManager
import android.graphics.Bitmap
import hypr.a255bits.com.hypr.Generator.Generator
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.experimental.GpuDelegate;
import java.nio.ByteBuffer

open class GeneratorLoader {
    lateinit var inference: Interpreter

    var generator: Generator? = null
    var channels: Int = 0
    var width: Int = 0
    var height: Int = 0
    var z_dimsArray: LongArray = longArrayOf()
    var z_dims: Long = 0
    var raw: FloatArray = floatArrayOf()
    var index: Int? = 0
    var assets: AssetManager? = null

    fun setIndex(index: Int) {
        this.index = index
    }

    fun loadGenerator(generator: Generator) {
        this.generator = generator
        this.width = generator.generator?.output?.width!!
        this.height = generator.generator!!.output?.height!!
        channels = generator.generator!!.output!!.channels!!
        z_dimsArray = generator.generator!!.input!!.z_dims!!.map { item -> item.toLong() }.toLongArray()
        z_dims = z_dimsArray.fold(1.toLong(), { mul, next -> mul * next })

        raw = FloatArray(width * height * channels)

    }

    fun load() {
        val file = generator?.model_file
        val asset = this.assets?.open( "generators/"+file)
        val bytes:ByteArray = asset!!.readBytes()
        val buffer = ByteBuffer.allocateDirect(bytes.size)
        buffer.put(bytes)
        val delegate = GpuDelegate();
        val options = Interpreter.Options().addDelegate(delegate)

        this.inference = Interpreter( buffer, options )
    }

    fun sample(z: FloatArray, mask: FloatArray, bitmap: Bitmap): IntArray {
        print("Sampling ")

        var inputs:Array<Any> = arrayOf(z)
        var outputs:HashMap<Int, Any> = hashMapOf(0 to this.raw)

        if(!this::inference.isInitialized) {
            this.load()
        }
        this.inference.runForMultipleInputsOutputs(inputs, outputs)


        return manipulatePixelsInBitmap()
    }

    fun mask(bitmap: Bitmap): FloatArray {
        //feedInput(bitmap)
        val floatValues = FloatArray(width * height)
        return floatValues
    }

    fun sampleRandom(z: FloatArray, mask: FloatArray, scaled: Bitmap): IntArray {

        val dims = longArrayOf(1.toLong(), 1.toLong(), 1.toLong(), 1.toLong())

        var inputs:Array<Any> = arrayOf(z)
        var outputs:HashMap<Int, Any> = hashMapOf(0 to this.raw)
        if(!this::inference.isInitialized) {
            this.load()
        }

        this.inference.runForMultipleInputsOutputs(inputs, outputs)

        return manipulatePixelsInBitmap()
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
        //feedInput(bitmap)


        val z = FloatArray(z_dims.toInt())


        return z
    }

    fun random_z(): FloatArray {
        val z = FloatArray(z_dims.toInt())
        val r = java.util.Random()
        for (i in 0..z_dims.toInt() - 1)
            z[i] = r.nextFloat() * 2.0f - 1.0f
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