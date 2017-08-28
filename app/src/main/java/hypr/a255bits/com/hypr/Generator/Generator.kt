package hypr.a255bits.com.hypr.Generator

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

class Generator() : Parcelable {
    @Json(name = "name")
    var name: String? = null

    @Json(name = "file_size_in_bytes")
    var fileSizeInBytes: Long? = null

    @Json(name = "model_url")
    var model_url: String? = null

    @Json(name = "generator")
    var generator: Generator_? = null

    @Json(name = "price_in_cents")
    var priceInCents: Int? = null

    @Json(name = "google_play_id")
    var google_play_id: String? = null

    constructor(source: Parcel) : this()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Generator> = object : Parcelable.Creator<Generator> {
            override fun createFromParcel(source: Parcel): Generator = Generator(source)
            override fun newArray(size: Int): Array<Generator?> = arrayOfNulls(size)
        }
    }
}
