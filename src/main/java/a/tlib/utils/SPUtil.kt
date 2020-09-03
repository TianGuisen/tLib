package a.tlib.utils

import a.tlib.LibApp
import android.content.Context
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


const val SPFileName = "default"
/**
 * 使用defSP的话key为""在SPUtil中key会赋值成变量名
 * 所以一般用这个
 */
inline fun <reified R, T> R.defSP(default: T) = SPUtil("", default)

/**
 * 指定key用这个
 */
inline fun <reified R, T> R.defSP(key: String, default: T) = SPUtil(key, default)

val sp by lazy {
    ///放在SPUtil外面只会加载一次，放在里面每次都会加载
    LibApp.app.getSharedPreferences(SPFileName, Context.MODE_PRIVATE)
}

class SPUtil<T>(val key: String, val defValue: T) : ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val temKey = if (key.isEmpty()) property.name else key
        return when (defValue) {
            is String -> sp.getString(temKey, defValue)
            is Boolean -> sp.getBoolean(temKey, defValue)
            is Int -> sp.getInt(temKey, defValue)
            is Float -> sp.getFloat(temKey, defValue)
            is Long -> sp.getLong(temKey, defValue)
            else -> throw IllegalArgumentException("类型错误")
        } as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val temKey = if (key.isEmpty()) property.name else key
        with(sp.edit()) {
            when (value) {
                is String -> putString(temKey, value)
                is Boolean -> putBoolean(temKey, value)
                is Int -> putInt(temKey, value)
                is Float -> putFloat(temKey, value)
                is Long -> putLong(temKey, value)
                else -> throw IllegalArgumentException("类型错误")
            }
            commit()
        }
    }
}

fun cleanSP(fileName: String = SPFileName) {
    LibApp.app.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit().clear().commit()
}
