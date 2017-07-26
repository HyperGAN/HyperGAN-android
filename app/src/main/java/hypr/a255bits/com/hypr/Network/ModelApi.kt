package hypr.a255bits.com.hypr.Network

import android.content.Context
import hypr.a255bits.com.hypr.Generator
import hypr.a255bits.com.hypr.R
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ModelApi(): ModelService{
    private val BASE_URL = "https://gist.githubusercontent.com"
    val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val service: ModelService? = retrofit.create(ModelService::class.java)

    override fun listOfModels(): Call<List<Generator>>? {
        val call = service?.listOfModels()
        return call
    }

}