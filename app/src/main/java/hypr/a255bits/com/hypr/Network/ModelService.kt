package hypr.a255bits.com.hypr.Network

import retrofit2.Call
import retrofit2.http.GET

interface ModelService{

    @GET("https://gist.githubusercontent.com/TedHoryczun/787f524fd2bdd260797cf6c8be854e05/raw/890546f047c78c7e9246d0bf47a572eab984a8c5/dummy_hypr.json")
    fun listOfModels(): Call<java.lang.String>
}

