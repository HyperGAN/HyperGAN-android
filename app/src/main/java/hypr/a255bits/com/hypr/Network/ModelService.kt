package hypr.a255bits.com.hypr.Network

import hypr.a255bits.com.hypr.Generator.Generator
import hypr.a255bits.com.hypr.Generator.Generator_
import retrofit2.Call
import retrofit2.http.GET

interface ModelService{

    @GET("https://gist.githubusercontent.com/TedHoryczun/787f524fd2bdd260797cf6c8be854e05/raw/7b34a1f0a4a4e8ad86a68306a616a83d78608a2b/dummy_hypr.json")
    fun listOfModels(): Call<List<Generator>>?
}

