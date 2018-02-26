package hypr.gan.com.hypr.Generator

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class Generator : Parcelable {
    @Json(name = "name")
    var name: String = ""
    @Json(name = "features")
    var features: List<String> = listOf()

    @Json(name = "file_size_in_bytes")
    var fileSizeInBytes: Long? = null

    @Json(name = "model_url")
    var model_url: String? = null

    @Json(name = "generator")
    var generator: Generator_? = Generator_()

    @Json(name = "price_in_cents")
    var priceInCents: Int? = null

    @Json(name = "google_play_id")
    var google_play_id: String = ""

    @Json(name = "image")
    var image: String = ""

    @Json(name = "description")
    var description: String = ""
}
