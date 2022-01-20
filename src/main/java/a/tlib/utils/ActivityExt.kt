package a.tlib.utils

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita

/**
 * @author 田桂森 2021/5/29 0029
 */

fun Activity.getBooleanExtra(key: String, default: Boolean = false): Boolean {
    return intent.getBooleanExtra(key, default)
}

/**
 * null会转为默认值""
 */
fun Activity.getStringExtra(key: String, default: String = ""): String {
    intent.getStringExtra(key).let {
        return if (it == null) default else it
    }
}

fun Activity.getLongExtra(key: String, defaultValue: Long = 0): Long {
    return intent.getLongExtra(key, defaultValue)
}

fun Activity.getDoubleExtra(key: String, defaultValue: Double = 0.0): Double {
    return intent.getDoubleExtra(key, defaultValue)
}

fun Activity.getIntExtra(key: String, default: Int = 0): Int {
    return intent.getIntExtra(key, default)
}

fun <T> Activity.getSerializableExtra(key: String): T? {
    intent.getSerializableExtra(key)?.let {
        return it as T
    }
    return null
}

fun <T> Activity.getListExtra(key: String): MutableList<T>? {
    intent.getSerializableExtra(key)?.let {
        return it as MutableList<T>
    }
    return mutableListOf()
}

