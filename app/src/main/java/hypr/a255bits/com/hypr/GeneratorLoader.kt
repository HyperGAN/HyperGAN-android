package hypr.a255bits.com.hypr
import android.content.res.AssetManager
import org.tensorflow.contrib.android.TensorFlowInferenceInterface

class Generator {}

class GeneratorLoader {
    fun load(assetPath:String, assetManager:AssetManager):Generator {
        print("Loading" + assetPath)
        val model:String = "model.pbgraph"
        val inference = TensorFlowInferenceInterface(assetManager, model)
        //TODO store
        return Generator()
    }

    fun sample(generator:Generator) {
        print("Sampling " + generator)
        //inference.run(..)
        //inference.getTensor(generator.outputNode)
    }

}