package a.tlib.utils;

sealed class BooleanExt<out T>

object Otherwise : BooleanExt<Nothing>()
class WithData<T>(val data: T) : BooleanExt<T>()

inline fun <T> Boolean?.yes(function: () -> T): BooleanExt<T> {
    if (this!=null&&this) {
        return WithData(function())
    } else {
        return Otherwise
    }
}


inline fun <T> Boolean?.no(function: () -> T): BooleanExt<T> {
    if (this!=null&&this) {
        return Otherwise
    } else {
        return WithData(function())
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <T> BooleanExt<T>?.otherwise(function: () -> T): T = when (this) {
    is Otherwise -> function()
    is WithData<T> -> this.data
    else -> function()
}
