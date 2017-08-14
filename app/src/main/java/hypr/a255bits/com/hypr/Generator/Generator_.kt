package hypr.a255bits.com.hypr.Generator

import com.squareup.moshi.Json

class Generator_ {

    @Json(name = "viewer")
    var viewer: Viewer? = null
    @Json(name = "input")
    var input: Input? = null
    @Json(name = "output")
    var output: Output? = null
}
