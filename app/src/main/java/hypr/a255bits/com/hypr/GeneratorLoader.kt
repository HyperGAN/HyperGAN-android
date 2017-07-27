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
        try {
            this.inference = TensorFlowInferenceInterface(assets, pbfile)
        } catch(e:Exception) {
            Log.d("generator", "ARGH" + e.message)
        }

    }
    fun sample():Bitmap {
        print("Sampling ")
        val bitmap = this.bitmap

        this.inference!!.run(arrayOf<String>("Tanh_2"))

        val intValues = intArrayOf(width * height)

        //inference.readNodeFloat(OUTPUT_NODE, resu)

        //inference.run(..)
        this.inference!!.fetch("Tanh_2", this.raw)

        for (i in 0..intValues.size - 1) {
            intValues[i] = 0xFF000000.toInt() or ((raw[i * 3] * 255) as Int shl 16) or ((raw[i * 3 + 1] * 255) as Int shl 8) or (raw[i * 3 + 2] * 255) as Int
        }
        bitmap!!.setPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return bitmap
    }

}