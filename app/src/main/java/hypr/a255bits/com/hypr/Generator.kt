package hypr.a255bits.com.hypr

import com.google.gson.annotations.SerializedName

class Generator {
    var name: String = ""
    @SerializedName("file_size_in_bytes")
    var fileSize: Long = 0
    @SerializedName("model_url")
    var modelUrl: String = ""
    var viewer: Viewer = Viewer()
    var input: Input = Input()
    var output: Output = Output()
}


class Viewer {
    var type: String = ""
    var operation: String = ""
    var controls: MutableList<String> = mutableListOf()
}

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



