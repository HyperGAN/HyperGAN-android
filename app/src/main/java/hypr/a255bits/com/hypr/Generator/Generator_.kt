package hypr.a255bits.com.hypr.Generator

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

    @Json(name = "uses_mask")
    var uses_mask: Boolean? = false
    @Json(name = "mask_input_node")
    var mask_input_node: String? = null

    @Json(name = "z_output_node")
    var z_output_node: String? = null
}
