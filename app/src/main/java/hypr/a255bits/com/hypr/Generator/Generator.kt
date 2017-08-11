package hypr.a255bits.com.hypr.Generator

import android.view.View
import com.squareup.moshi.Json


class Generator(val viewer: Viewer, val input: Input) {
    var name: String = ""
    @Json(name = "file_size_in_bytes")var fileSize: Long = 0
    @Json(name = "model_url")var modelUrl: String = ""
    var output: Output? = null
}

class Viewer(val type: String, val operation: String, val controls: List<Control>)


class Input {
    var type: String = "image"
    var width: Int = 0
    var height: Int = 0
    var channels: Int = 0
}

class Output {
    var type: String = "image"
    var width: Int = 0
    var height: Int = 0
    var channels: Int = 0
}



