package hypr.a255bits.com.hypr

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class BuyGenerator(val name: String, val imageLocation: String) : Parcelable {
    var itemBought = false
}