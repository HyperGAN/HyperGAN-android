package hypr.a255bits.com.hypr.Network

import hypr.a255bits.com.hypr.Generator.Generator
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ModelApi() : ModelService{
    private val BASE_URL = "https://gist.githubusercontent.com"
    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    val service: ModelService? = retrofit.create(ModelService::class.java)

    override fun listOfModels(): Call<List<Generator>>? {
        val call = service?.listOfModels()
        return call
    }

}