package hypr.a255bits.com.hypr.Network

import hypr.a255bits.com.hypr.Generator
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ModelApi: ModelService{
    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val service: ModelService? = retrofit.create(ModelService::class.java)

    override fun listOfModels(): Call<List<Generator>>? {
        val call = service?.listOfModels()
        return call
    }

}