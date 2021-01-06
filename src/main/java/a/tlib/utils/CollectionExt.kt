package a.tlib.utils

/**
 * @author 田桂森 2020/4/20 0020
 */
/**
 * list不是null且size>0
 */
fun <T> Collection<T>?.isNotNullEmpty(): Boolean = this == null || !isEmpty()

public fun <T> List<T>.last(): T? {
    if (isEmpty()) return null
    return this[lastIndex]
}