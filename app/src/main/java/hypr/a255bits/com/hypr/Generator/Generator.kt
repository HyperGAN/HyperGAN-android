package hypr.a255bits.com.hypr.Generator

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class Generator : Parcelable {
    @Json(name = "name")
    var name: String = ""

    @Json(name = "model_url")
    var model_url: String? = null

    @Json(name = "generator")
    var generator: Generator_? = null

    @Json(name = "google_play_id")
    var google_play_id: String = ""
}
