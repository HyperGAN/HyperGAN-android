package hypr.hypergan.com.hypr.Main

import hypr.hypergan.com.hypr.Generator.Generator


interface GeneratorListener {
    fun getGenerators(generators: List<Generator>, index: Int)
}