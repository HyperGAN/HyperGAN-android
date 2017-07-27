package hypr.a255bits.com.hypr

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.contrib.android.TensorFlowInferenceInterface

class GeneratorLoader() {
    public var inference:TensorFlowInferenceInterface? = null

    var channels = 3
    var width = 128
    var height = 128
    public var bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

    public var raw:FloatArray = FloatArray(width*height*channels)

    fun load(assets:AssetManager) {
        System.loadLibrary("tensorflow_inference")

        var pbfile:String = "file:///android_asset/generators/optimized_weight_conv.pb"
        this.inference = TensorFlowInferenceInterface(assets, pbfile)


    }
    fun sample():Bitmap {
        print("Sampling ")

        this.inference!!.run(arrayOf<String>("Tanh_1"))

        val intValues = IntArray(width * height)

        //inference.readNodeFloat(OUTPUT_NODE, resu)

        //inference.run(..)
        this.inference!!.fetch("Tanh_1", this.raw)

        for (i in 0..intValues.size - 1) {

            val raw_one:Int = (((raw[i * 3]+1)/2.0 * 255).toInt()) shl 16
            val raw_two:Int = (((raw[i * 3 + 1]+1)/2.0 * 255).toInt()) shl 8
            val raw_three:Int = ((raw[i * 3 + 2]+1)/2.0 * 255).toInt()
            intValues[i] = 0xFF000000.toInt() or raw_one or raw_two or raw_three
        }
        bitmap!!.setPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return bitmap
    }

}