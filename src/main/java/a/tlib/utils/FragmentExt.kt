@file:Suppress("unused")

package a.tlib.utils

import androidx.fragment.app.Fragment
import java.io.Serializable

/**
 * Fragment extensions
 *
 * @author LiuBin
 * @version v1.0, 2019-05-23 15:48  由有播科技（杭州）有限公司开发
 */
fun Fragment.getStringArgument(key: String): String {
    return this.arguments?.getString(key) ?: ""
}

fun Fragment.getIntArgument(key: String, defaultValue: Int = 0): Int {
    return this.arguments?.getInt(key, defaultValue) ?: defaultValue
}

fun Fragment.getLongArgument(key: String, defaultValue: Long = 0): Long {
    return this.arguments?.getLong(key, defaultValue) ?: defaultValue
}

fun Fragment.getDoubleArgument(key: String, defaultValue: Double = 0.0): Double {
    return this.arguments?.getDouble(key, defaultValue) ?: defaultValue
}

fun Fragment.getBooleanArgument(key: String, defaultValue: Boolean = false): Boolean {
    return this.arguments?.getBoolean(key, defaultValue) ?: defaultValue
}

fun <T> Fragment.getSerializableArgument(key: String): T? {
    this.arguments?.getSerializable(key)?.let {
        return it as T
    }
    return null
}

fun <T> Fragment.getListArgument(key: String): MutableList<T>? {
    this.arguments?.getSerializable(key)?.let {
        return it as MutableList<T>
    }
    return mutableListOf()
}