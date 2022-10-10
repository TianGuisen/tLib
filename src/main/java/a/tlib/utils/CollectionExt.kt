package a.tlib.utils

import a.tlib.logger.YLog
import android.os.Build
import androidx.annotation.RequiresApi


/**
 * @author 田桂森 2020/4/20 0020
 */
/**
 * list不是null且size>0
 */
fun <T> Collection<T>?.isNotNullEmpty(): Boolean = !this.isNullOrEmpty()


fun <T> List<T>?.isNotNullEmpty(function: (List<T>) -> Unit) = (!this.isNullOrEmpty()).let { this?.let { function(it) } }

fun <T> List<T>.last(): T? {
    if (isEmpty()) return null
    return this[lastIndex]
}

fun <K, V> Map<K, V>?.isNotNullEmply(): Boolean = !this.isNullOrEmpty()

/**
 * null安全
 */
fun <T> MutableCollection<T>?.addSafe(data: T?) {
    if (data != null) {
        this?.add(data)
    }
}

/**
 * 取list中某个属性的值用逗号进行拼接，默认取id
 * 通常用于后台接口传拼接字符串
 */
fun Collection<Any>.spliceStr(idName: String="id"): String {
    var str = ""
    forEach {
        val field = it.javaClass.getDeclaredField(idName)
        field.setAccessible(true)
        str = str + field.get(it).toString() + ","
    }
    str = str.delLast()
    return str
}