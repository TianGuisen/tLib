package a.tlib.utils

/**
 * @author 田桂森 2020/4/20 0020
 */
/**
 * list不是null且size>0
 */
fun <T> Collection<T>?.isNotNullEmpty(): Boolean = !this.isNullOrEmpty()

fun <T> List<T>.last(): T? {
    if (isEmpty()) return null
    return this[lastIndex]
}

fun <K, V> Map<K, V>?.isNotNullEmply(): Boolean = !this.isNullOrEmpty()

/**
 * null安全
 */
fun  <T> MutableCollection<T>?.addSafe(data:T?){
    if (data!=null){
        this?.add(data)
    }
}