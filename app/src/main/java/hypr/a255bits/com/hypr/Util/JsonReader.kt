package hypr.a255bits.com.hypr.Util

import android.content.Context

class JsonReader{

    fun readJson(context: Context): String {
        val fileContents: String = context.assets.open("generatorJson.json").bufferedReader().use { it.readText() }
        return fileContents
    }
}