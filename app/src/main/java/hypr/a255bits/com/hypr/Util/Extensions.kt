package hypr.a255bits.com.hypr.Util

fun Float.nonNegativeInt(): Int{
    return intArrayOf(this.toInt(), 0).max()!!
}

fun Int.negative1To1(): Double {
    return ((this - 100) / 100.00)
}
