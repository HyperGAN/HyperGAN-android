package hypr.a255bits.com.hypr.Util

import android.content.Context
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import hypr.a255bits.com.hypr.Generator.Generator
import java.lang.reflect.Type

class JsonReader{

    fun getGeneratorsFromJson(context: Context): List<Generator>? {
       val json = readJson(context)
        val type: Type = Types.newParameterizedType(List::class.java, Generator::class.java)
        val adapter: JsonAdapter<List<Generator>> = Moshi.Builder().build().adapter(type)
        return adapter.fromJson(json)
    }

    fun readJson(context: Context): String {
        return context.assets.open("generatorJson.json").bufferedReader().use { it.readText() }
    }
}