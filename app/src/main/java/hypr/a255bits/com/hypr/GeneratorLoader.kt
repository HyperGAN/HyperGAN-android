package hypr.a255bits.com.hypr


class Generator {}

class GeneratorLoader {
    fun load(assetPath:String):Generator {
        print("Loading" + assetPath)
        return Generator()
    }

    fun sample(generator:Generator) {
        print("Sampling " + generator)
    }

}