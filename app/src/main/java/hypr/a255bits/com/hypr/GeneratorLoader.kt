package hypr.a255bits.com.hypr
import android.content.res.AssetManager
import org.tensorflow.contrib.android.TensorFlowInferenceInterface


class GeneratorLoader(assetPath:String, assetManager:AssetManager) {

    var inference:TensorFlowInferenceInterface = TensorFlowInferenceInterface(assetManager, assetPath)

    fun sample(generator:Generator) {
        print("Sampling " + generator)
        //inference.run(..)
        //inference.getTensor(generator.outputNode)
    }

}