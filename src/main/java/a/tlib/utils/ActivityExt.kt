package a.tlib.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import java.util.*

/**
 * @author 田桂森 2022-7-6
 */

/**
 * 跳转activity
 */
fun startAct(intent: Intent) = ActStackManager.getTopActivity().startActivity(intent)

/**
 * 跳转activity
 */
inline fun <reified T : Activity> startAct(
        vararg pairs: Pair<String, Any?>,
        crossinline block: Intent.() -> Unit = {}
) =
        ActStackManager.getTopActivity().startAct<T>(pairs = pairs, block = block)


inline fun <reified T : Activity> Context.startAct(
        vararg pairs: Pair<String, Any?>,
        crossinline block: Intent.() -> Unit = {}
) =
        startActivity(intentOf<T>(*pairs).apply(block))

/**
 * 获取可能为null的intent数据
 * val s by extras<String>(S)
 */
fun <T> Activity.extras(key: String) = lazy<T?> {
    intent.extras?.get(key)?.let {
        return@lazy it as T
    }
    null
}

/**
 * 获取可能为null的intent数据，如果获取不到，取default默认值
 * val s by extras<String>(S,"")
 */
fun <T> Activity.extras(key: String, default: T) = lazy {
    intent.extras?.get(key)?.let {
        return@lazy it as T
    }
    default
}

/**
 * 获取安全intent数据，强制不为null，是null会抛异常
 * val s by safeExtras<String>(S)
 */
fun <T> Activity.safeExtras(key: String) = lazy {
    intent.extras?.get(key) as T
}

inline fun <reified T : Activity> ActivityResultLauncher<Intent>.startAct(vararg pairs: Pair<String, *>) =
        launch(ActStackManager.getTopActivity().intentOf<T>(*pairs))

fun ActivityResultCaller.registerForStartActivityResult(callback: ActivityResultCallback<ActivityResult>) =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult(), callback)
