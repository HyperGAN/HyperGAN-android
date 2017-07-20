package hypr.a255bits.com.hypr.Network

import hypr.a255bits.com.hypr.Generator
import retrofit2.Call
import retrofit2.http.GET

interface ModelService{

    @GET("https://gist.githubusercontent.com/martyn/" +
            "6e726329c666fe64a48167b71974298a/raw/2e3962e9239c81139a4d3bf9100e47be17a6d34b/generator-list.json")
    fun listOfModels(): Call<List<Generator>>?
}

