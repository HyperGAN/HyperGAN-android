package hypr.a255bits.com.hypr.Main

import hypr.a255bits.com.hypr.Generator


interface GeneratorListener {
    fun getGenerator(generator: Generator, index: Int)
}