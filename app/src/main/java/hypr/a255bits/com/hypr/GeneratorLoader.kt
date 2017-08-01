package hypr.a255bits.com.hypr

import android.content.res.AssetManager
import android.graphics.Bitmap
import org.tensorflow.contrib.android.TensorFlowInferenceInterface

class GeneratorLoader {
    lateinit var inference: TensorFlowInferenceInterface

    var channels = 3
    var width = 128
    var height = 128
    var z_dims = 1 * 16 * 16 * 96

    var raw: FloatArray = FloatArray(width * height * channels)

    fun load(assets: AssetManager) {
        System.loadLibrary("tensorflow_inference")

        val pbfile: String = "file:///android_asset/generators/optimized_weight_conv.pb"
        this.inference = TensorFlowInferenceInterface(assets, pbfile)


    }

    fun sample(z:FloatArray): Bitmap {
        print("Sampling ")


        var dims = longArrayOf(1.toLong(), 16.toLong(), 16.toLong(), 96.toLong())
        this.inference.feed(

                "concat", z, *dims);

        this.inference.run(arrayOf("Tanh_1"))
        //inference.readNodeFloat(OUTPUT_NODE, resu)

        //inference.run(..)
        this.inference.fetch("Tanh_1", this.raw)

        val pixelsInBitmap = manipulatePixelsInBitmap()
        val bitmap = manipulateBitmap(width, height, pixelsInBitmap)
        return bitmap
    }

    fun encode(bitmap: Bitmap): FloatArray {
        val intValues = IntArray(width * height)
        val floatValues = FloatArray(width * height * channels)
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (i in 0..intValues.size - 1) {
            val ival = intValues[i]
            floatValues[i * 3] = (ival shr 16 and 0xFF) / 255.0f
            floatValues[i * 3 + 1] = (ival shr 8 and 0xFF) / 255.0f
            floatValues[i * 3 + 2] = (ival and 0xFF) / 255.0f
        }
        var dims = longArrayOf(1.toLong(), width.toLong(), height.toLong(), channels.toLong())
        this.inference.feed(

                "input", floatValues, *dims);

        this.inference.run(arrayOf("Tanh"))

        var z: FloatArray = FloatArray(z_dims)

        this.inference.fetch("Tanh", z)

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

    private fun manipulateBitmap(width: Int, height: Int, pixelsInBitmap: IntArray): Bitmap {
        val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixelsInBitmap, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        return bitmap
    }

}