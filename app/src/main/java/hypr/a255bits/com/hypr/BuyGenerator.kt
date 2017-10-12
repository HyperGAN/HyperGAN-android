package hypr.a255bits.com.hypr

import android.os.Parcel
import android.os.Parcelable


data class BuyGenerator(val name: String) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
    }

}