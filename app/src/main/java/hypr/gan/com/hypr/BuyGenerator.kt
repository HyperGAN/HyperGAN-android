package hypr.gan.com.hypr

import android.os.Parcel
import android.os.Parcelable


data class BuyGenerator(val name: String) : Parcelable {
    var itemBought = false

    constructor(source: Parcel) : this(
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BuyGenerator> = object : Parcelable.Creator<BuyGenerator> {
            override fun createFromParcel(source: Parcel): BuyGenerator = BuyGenerator(source)
            override fun newArray(size: Int): Array<BuyGenerator?> = arrayOfNulls(size)
        }
    }
}