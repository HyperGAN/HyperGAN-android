package hypr.a255bits.com.hypr.Network

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import hypr.a255bits.com.hypr.Generator.Generator
import java.io.File
import java.lang.reflect.Type

class ModelApi(val service: ModelService){
    private val BASE_URL = "https://gist.githubusercontent.com"

    val generatorFileName = "generatorJson.json"

    fun listOfModels(context: Context): List<Generator>? {
        val type: Type = Types.newParameterizedType(List::class.java, Generator::class.java)
        val adapter: JsonAdapter<List<Generator>> = Moshi.Builder().build().adapter(type)
        val cachedFile = File(context.cacheDir, generatorFileName)
        return if (cachedFile.exists()) {
            getCachedGenerator(adapter, cachedFile)
        } else {
            getGeneratorFromNetwork(adapter, cachedFile)
        }
    }

    private fun getGeneratorFromNetwork(adapter: JsonAdapter<List<Generator>>, cachedFile: File): List<Generator>? {
        val jsonString: String? = service?.listOfModels()?.execute()?.body().toString()
        val json = adapter.fromJson(jsonString)
        cacheGenerators(jsonString!!, cachedFile)
        return json
    }

    private fun getCachedGenerator(adapter: JsonAdapter<List<Generator>>, cachedFile: File): List<Generator>? {
        val cachedFileRead = cachedFile.readText()
        return adapter.fromJson(cachedFileRead)
    }

    private fun cacheGenerators(generatorJson: String, cachedFile: File) {
        cachedFile.bufferedWriter().use { out ->
            generatorJson.forEach { text -> out.write(text.toString()) }
        }
    }
}