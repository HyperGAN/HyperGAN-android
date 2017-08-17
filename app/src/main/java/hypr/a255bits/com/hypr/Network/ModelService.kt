package hypr.a255bits.com.hypr.Network

import hypr.a255bits.com.hypr.Generator.Generator
import retrofit2.Call
import retrofit2.http.GET

interface ModelService{

    @GET("https://gist.githubusercontent.com/TedHoryczun/787f524fd2bdd260797cf6c8be854e05/raw/a4a026d98b55172a9a3db4bddcfb54ad9ab72431/dummy_hypr.json")
    fun listOfModels(): Call<List<Generator>>?
}

