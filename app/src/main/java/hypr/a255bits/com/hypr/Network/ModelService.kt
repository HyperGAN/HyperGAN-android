package hypr.a255bits.com.hypr.Network

import retrofit2.Call
import retrofit2.http.GET

interface ModelService{

    @GET("https://gist.githubusercontent.com/TedHoryczun/787f524fd2bdd260797cf6c8be854e05/raw/0f65031113b008d2fbf365c7cecbfd9cf0e6e4eb/dummy_hypr.json")
    fun listOfModels(): Call<java.lang.String>
}

