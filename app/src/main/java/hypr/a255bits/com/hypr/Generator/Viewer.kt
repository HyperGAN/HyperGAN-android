package hypr.a255bits.com.hypr.Generator

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class Viewer: Parcelable {

    @Json(name = "type")
    var type: String? = null
    @Json(name = "operation")
    var operation: String? = null
    @Json(name = "controls")
    var controls: List<Control>? = null

}
