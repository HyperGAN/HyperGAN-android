package hypr.a255bits.com.hypr.Generator

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

class Control() : Parcelable {
    @Json(name = "type")
    var type: String? = null

    @Json(name = "direction")
    var direction: List<Double>? = null

    @Json(name = "name")
    var name: String? = null

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Control> = object : Parcelable.Creator<Control> {
            override fun createFromParcel(source: Parcel): Control = Control(source)
            override fun newArray(size: Int): Array<Control?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {}
}
