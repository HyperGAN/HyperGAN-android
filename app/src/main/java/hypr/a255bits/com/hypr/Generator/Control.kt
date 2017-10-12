package hypr.a255bits.com.hypr.Generator

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class Control : Parcelable {
    @Json(name = "type")
    var type: String? = null

    @Json(name = "name")
    var name: String? = null
}
