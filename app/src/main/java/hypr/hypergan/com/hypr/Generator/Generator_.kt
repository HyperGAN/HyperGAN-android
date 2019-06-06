package hypr.hypergan.com.hypr.Generator

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
class Generator_: Parcelable{

    @Json(name = "viewer")
    var viewer: Viewer? = null
    @Json(name = "input")
    var input: Input? = null
    @Json(name = "output")
    var output: Output? = null
}
