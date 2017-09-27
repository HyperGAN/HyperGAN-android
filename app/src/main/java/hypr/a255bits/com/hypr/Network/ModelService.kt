package hypr.a255bits.com.hypr.Network

import retrofit2.Call
import retrofit2.http.GET

interface ModelService{

    @GET("https://gist.githubusercontent.com/TedHoryczun/787f524fd2bdd260797cf6c8be854e05/raw/16daee161098fc3977fd1e08dff2c9ef81c5f373/dummy_hypr.json")
    fun listOfModels(): Call<java.lang.String>
}

