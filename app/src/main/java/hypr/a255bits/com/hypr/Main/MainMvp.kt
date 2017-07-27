package hypr.a255bits.com.hypr.Main

import hypr.a255bits.com.hypr.Generator

interface MainMvp {
    interface view{
        fun  modeToNavBar(generator: Generator, index: Int)
        fun  startModelFragment(indexInJson: Int)
        fun  applyModelToImage(modelUrl: String, image: ByteArray?)
        fun startModelOnImage()

    }
    interface presenter{

        fun addModelsToNavBar()
        fun  startModel(itemId: Int)
        fun  startModel(itemId: Int, image: ByteArray?)
    }

    interface interactor {
        fun addModelsToNavBar(param: GeneratorListener)

    }

}
