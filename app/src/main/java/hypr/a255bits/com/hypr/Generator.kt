package hypr.a255bits.com.hypr

class Generator {
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



