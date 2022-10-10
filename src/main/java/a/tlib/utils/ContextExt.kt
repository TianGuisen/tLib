package a.tlib.utils

import a.tlib.utils.retrofit.bindIo
import android.app.Activity
import android.content.Intent
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import io.reactivex.Single

/**
 * @author 田桂森 2020/2/22
 * enableAnimation默认开启动画，某些场景会有bug要关闭
 */

/**
 * 获取viewModel
 */
inline fun <reified T : ViewModel> androidx.lifecycle.LifecycleOwner.getVM(): T {
    return vita.with(VitaOwner.Multiple(this)).getViewModel<T>()
}

/**
 * 简单的切换线程，防止内存泄漏
 */
inline fun androidx.lifecycle.LifecycleOwner.runIo(crossinline func: () -> Unit) {
    Single.just(0).bindIo(this).subscribe { _ ->
        func()
    }
}

/**
 * 关闭页面返回数据，resultCode=Activity.RESULT_OK
 * finishWithResult("a" to "a")
 */
fun Activity.finishWithResult(vararg pairs: Pair<String, *>) {
    if (pairs.isEmpty()) {
        setResult(Activity.RESULT_OK)
    } else {
        setResult(Activity.RESULT_OK, Intent().putExtras(bundleOf(*pairs)))
    }
    finish()
}

/**
 * 关闭页面返回数据，带resultCode
 * finishWithResult(10,"a" to "a")
 */
fun Activity.finishWithResult(resultCode: Int = Activity.RESULT_OK, vararg pairs: Pair<String, *>) {
    if (pairs.isEmpty()) {
        setResult(resultCode)
    } else {
        setResult(resultCode, Intent().putExtras(bundleOf(*pairs)))
    }
    finish()
}

