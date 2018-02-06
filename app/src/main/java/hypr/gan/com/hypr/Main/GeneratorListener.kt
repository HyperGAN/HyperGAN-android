package hypr.gan.com.hypr.Main

import hypr.gan.com.hypr.Generator.Generator


interface GeneratorListener {
    fun getGenerators(generators: List<Generator>, index: Int)
}