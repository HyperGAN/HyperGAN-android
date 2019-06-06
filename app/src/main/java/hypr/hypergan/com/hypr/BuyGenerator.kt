package hypr.hypergan.com.hypr

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class BuyGenerator(val name: String) : Parcelable {
    var itemBought = false

}