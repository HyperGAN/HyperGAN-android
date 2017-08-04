package hypr.a255bits.com.hypr.Main

import hypr.a255bits.com.hypr.Generator


interface GeneratorListener {
    fun getGenerators(generators: List<Generator>, index: Int)
}