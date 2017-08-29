package hypr.a255bits.com.hypr.Util

/**
 * Created by tedho on 8/29/2017.
 */
fun Float.nonNegativeInt(): Int{
    return intArrayOf(this.toInt(), 0).max()!!
}
