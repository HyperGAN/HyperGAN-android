package hypr.a255bits.com.hypr.Generator

import android.os.Parcel
import android.os.Parcelable


class Control() : Parcelable {
    val type: String = ""

    val direction: List<Double> = listOf()

    val name: String = ""

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Control> = object : Parcelable.Creator<Control> {
            override fun createFromParcel(source: Parcel): Control = Control(source)
            override fun newArray(size: Int): Array<Control?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {}
}