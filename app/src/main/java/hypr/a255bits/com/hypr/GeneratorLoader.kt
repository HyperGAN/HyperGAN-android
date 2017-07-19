package hypr.a255bits.com.hypr
import org.tensorflow.contrib.android.TensorflowInferenceInterface

class Generator {}

class GeneratorLoader {
    fun load(assetPath:String):Generator {
        print("Loading" + assetPath)
        //model : String  = ...
        //manager:AssetManager=...
        //inference:TensorflowInferenceInterface = TensorflowInferenceInterface(manager, string)
        return Generator()
    }

    fun sample(generator:Generator) {
        print("Sampling " + generator)
        //inference.run(..)
        //inference.getTensor(generator.outputNode)
    }

}