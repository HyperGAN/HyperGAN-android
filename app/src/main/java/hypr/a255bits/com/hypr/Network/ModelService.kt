package hypr.a255bits.com.hypr.Network

import retrofit2.Call
import retrofit2.http.GET

interface ModelService{

    @GET("https://gist.githubusercontent.com/TedHoryczun/787f524fd2bdd260797cf6c8be854e05/raw/c0dce4bf228cab3bce0dec662ce2dc8bf05396df/dummy_hypr.json")
    fun listOfModels(): Call<java.lang.String>
}

