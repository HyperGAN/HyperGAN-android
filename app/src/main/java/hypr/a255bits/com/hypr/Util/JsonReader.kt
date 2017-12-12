package hypr.a255bits.com.hypr.Util

import android.content.Context
import com.squareup.moshi.JsonAdapter
import hypr.a255bits.com.hypr.Generator.Generator

class JsonReader(val adapter: JsonAdapter<List<Generator>>){

    fun getGeneratorsFromJson(context: Context): List<Generator>? {
       val json = readJson(context)
        return adapter.fromJson(json)
    }

    fun readJson(context: Context): String {
        return context.assets.open("generatorJson.json").bufferedReader().use { it.readText() }
    }
}