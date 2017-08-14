package hypr.a255bits.com.hypr.Generator

import com.squareup.moshi.Json

class Generator {

    @Json(name = "name")
    var name: String? = null
    @Json(name = "file_size_in_bytes")
    var fileSizeInBytes: Int? = null
    @Json(name = "model_url")
    var modelUrl: String? = null
    @Json(name = "generator")
    var generator: Generator_? = null
    @Json(name = "price_in_cents")
    var priceInCents: Int? = null

}
