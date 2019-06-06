package hypr.hypergan.com.hypr.Network

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import hypr.hypergan.com.hypr.Generator.Generator
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.lang.reflect.Type

class ModelApi {
    private val BASE_URL = "https://gist.githubusercontent.com"
    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    val service: ModelService? = retrofit.create(ModelService::class.java)
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