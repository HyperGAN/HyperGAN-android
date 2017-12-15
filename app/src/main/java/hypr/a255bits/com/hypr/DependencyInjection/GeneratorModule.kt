package hypr.a255bits.com.hypr.DependencyInjection

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.GeneratorLoader.EasyGeneratorLoader
import hypr.a255bits.com.hypr.Network.ModelService
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.Type

class GeneratorModule {
    private fun getModelApiDependencies(): ModelService {
        val BASE_URL = "https://gist.githubusercontent.com"
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        return retrofit.create(ModelService::class.java)
    }

    fun getJsonAdapter(): JsonAdapter<List<Generator>> {
        val type: Type = Types.newParameterizedType(List::class.java, Generator::class.java)
        return Moshi.Builder().build().adapter(type)
    }

    fun getGeneratorLoader(): EasyGeneratorLoader {
        return EasyGeneratorLoader(Generator())
    }

}