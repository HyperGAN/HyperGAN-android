package hypr.a255bits.com.hypr.Generator

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class Output: Parcelable {

    @Json(name = "type")
    var type: String? = null
    @Json(name = "width")
    var width: Int? = null
    @Json(name = "height")
    var height: Int? = null

}
