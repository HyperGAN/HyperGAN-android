package hypr.a255bits.com.hypr.Generator

import com.squareup.moshi.Json

class Input {

    @Json(name = "type")
    var type = ""
    @Json(name = "width")
    var width: Int = 0
    @Json(name = "height")
    var height: Int = 0
    @Json(name = "channels")
    var channels: Int = 0
    @Json(name = "z_dims")
    var z_dims =  longArrayOf()

}
