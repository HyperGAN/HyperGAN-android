package hypr.a255bits.com.hypr.DependencyInjection

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.GeneratorLoader.EasyGeneratorLoader
import hypr.a255bits.com.hypr.GeneratorLoader.GeneratorFacePosition
import hypr.a255bits.com.hypr.ModelFragmnt.ModelFragmentPresenter
import hypr.a255bits.com.hypr.Network.ModelApi
import hypr.a255bits.com.hypr.Network.ModelService
import hypr.a255bits.com.hypr.Util.JsonReader
import org.koin.android.module.AndroidModule
import org.koin.dsl.context.Context
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.Type

class GeneratorModule : AndroidModule() {
    override fun context(): Context {
        return applicationContext {
            context("generator") {
                provide { getGeneratorLoader() }
                provide { ModelFragmentPresenter(get()) }
                provide { GeneratorFacePosition() }
                provide { JsonReader(getJsonAdapter()) }

                provide { ModelApi(getModelApiDependencies()) }
            }
        }
    }

    private fun getModelApiDependencies(): ModelService {
        val BASE_URL = "https://gist.githubusercontent.com"
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        return retrofit.create(ModelService::class.java)
    }

    private fun getJsonAdapter(): JsonAdapter<List<Generator>> {
        val type: Type = Types.newParameterizedType(List::class.java, Generator::class.java)
        return Moshi.Builder().build().adapter(type)
    }

    fun getGeneratorLoader(): EasyGeneratorLoader {
        return EasyGeneratorLoader(Generator())
    }

}