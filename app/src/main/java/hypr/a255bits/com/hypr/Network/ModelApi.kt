package hypr.a255bits.com.hypr.Network

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import hypr.a255bits.com.hypr.Generator.Generator
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type

class ModelApi {
    private val BASE_URL = "https://gist.githubusercontent.com"
    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    val service: ModelService? = retrofit.create(ModelService::class.java)
    val generatorFile = createTempFile("generatorJson", "json")

    fun listOfModels(): Deferred<List<Generator>?> {
        val type: Type = Types.newParameterizedType(List::class.java, Generator::class.java)
        val adapter: JsonAdapter<List<Generator>> = Moshi.Builder().build().adapter(type)
        return if (cachedFileExists()) {
            getCachedGenerator(adapter)
        } else {
            getGeneratorFromNetwork(adapter)
        }
    }

    private fun getGeneratorFromNetwork(adapter: JsonAdapter<List<Generator>>): Deferred<List<Generator>?> {
        val call = service?.listOfModels()
        return async(UI) {
            adapter.fromJson(call?.execute()?.body())
        }
    }

    private fun getCachedGenerator(adapter: JsonAdapter<List<Generator>>): Deferred<List<Generator>?> {
        val text = generatorFile.readText()
        return async(UI) {
            adapter.fromJson(text)
        }
    }

    private fun cacheGenerators(generatorJson: String) {
        generatorFile.bufferedWriter().use { out ->
            generatorJson.forEach { text -> out.write(text.toString()) }
        }
    }

    private fun cachedFileExists(): Boolean {
        return generatorFile.exists()
    }

}