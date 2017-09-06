package hypr.a255bits.com.hypr.Util

fun Float.nonNegativeInt(): Int{
    return intArrayOf(this.toInt(), 0).max()!!
}
