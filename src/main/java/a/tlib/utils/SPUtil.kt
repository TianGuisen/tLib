package a.tlib.utils

import a.tlib.LibApp
import android.content.Context
import android.content.SharedPreferences
import com.tencent.mmkv.MMKV
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
//    LibApp.app.getSharedPreferences(SPFileName, Context.MODE_PRIVATE)
    MMKV.initialize(LibApp.app)
    MMKV.defaultMMKV()!!
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
        with(sp) {
            when (value) {
                is String -> putString(temKey, value)
                is Boolean -> putBoolean(temKey, value)
                is Int -> putInt(temKey, value)
                is Float -> putFloat(temKey, value)
                is Long -> putLong(temKey, value)
                else -> throw IllegalArgumentException("类型错误")
            }
        }
    }
}

/**
 * SharedPreferences的旧数据迁移到mmkv
 */
fun migrate(migrateSp:MMKV,preferences: SharedPreferences){
    val kvs = preferences.all
    if (kvs != null && kvs.isNotEmpty()) {
        val iterator = kvs.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val key = entry.key
            val value = entry.value
            if (key != null && value != null) {
                migrateSp.run {
                    when (value) {
                        is Boolean -> this.putBoolean(key, value)
                        is Int ->  this.putInt(key,value)
                        is Long -> this.putLong(key,value)
                        is Float -> this.putFloat(key, value)
                        is String -> this.putString(key,value)
                        else -> {}
                    }
                }
            }
        }
        cleanSP()
    }
}
fun cleanSP(fileName: String = SPFileName) {
    LibApp.app.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit().clear().apply()
}
