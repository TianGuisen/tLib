package a.tlib.utils

import a.tlib.bean.BaseData
import a.tlib.bean.ShareElementInfo4
import a.tlib.utils.IntentUtil.createIntent
import a.tlib.utils.IntentUtil.fillBundleArguments
import a.tlib.utils.IntentUtil.fillIntentArguments
import a.tlib.utils.IntentUtil.internalStartActivityForResult
import a.tlib.utils.retrofit.bindIo
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.androidisland.vita.VitaOwner
import com.androidisland.vita.vita
import com.hw.ycshareelement.YcShareElement
import com.hw.ycshareelement.transition.ShareElementInfo
import io.reactivex.Single
import java.util.*

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

inline fun <reified T : Activity> Context.startAct(vararg params: Pair<String, Any?>) =
        this.startActivity(createIntent(this, T::class.java, params))

/**
 * 带动画跳转
 * android:transitionName="simple_img"
 */
inline fun <reified T : Activity> Activity.startAct(view: View, cover: String, vararg params: Pair<String, Any?>) {
    val optionsBundle = YcShareElement.buildOptionsBundle(this) {
        arrayOf<ShareElementInfo<*>>(ShareElementInfo(view, ShareElementInfo4(cover, AppUtil.getScreenWidth(), AppUtil.getScreenHeight())))
    }
    this.startActivity(createIntent(this, T::class.java, params), optionsBundle)
}


fun Context.startAct(intent: Intent, vararg params: Pair<String, Any?>) {
    fillIntentArguments(intent, params)
    startActivity(intent)
}

inline fun <reified T : Activity> Fragment.startAct(vararg params: Pair<String, Any?>, enableAnimation: Boolean = false) =
        this.startActivity(createIntent(this.requireActivity(), T::class.java, params))

inline fun <reified T : Activity> Activity.startActForResult(requestCode: Int, vararg params: Pair<String, Any?>, enableAnimation: Boolean = true) =
        internalStartActivityForResult(this, T::class.java, requestCode, params, enableAnimation)

inline fun <reified T : Activity> Fragment.startActForResult(requestCode: Int, vararg params: Pair<String, Any?>) =
        startActivityForResult(createIntent(activity!!, T::class.java, params), requestCode)

inline fun <reified T : Any> Context.intentFor(vararg params: Pair<String, Any?>): Intent =
        createIntent(this, T::class.java, params)

inline fun <reified T : Any> Fragment.intentFor(vararg params: Pair<String, Any?>): Intent =
        createIntent(activity!!, T::class.java, params)

inline fun <reified T : Fragment> Fragment.putData(vararg params: Pair<String, Any?>): T {
    val bundle = Bundle()
    fillBundleArguments(bundle, params)
    arguments = bundle
    return this as T
}

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.clearTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_TOP] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.clearTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }

/**
 * Add the [Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.clearWhenTaskReset(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET) }

/**
 * Add the [Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.excludeFromRecents(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS) }

/**
 * Add the [Intent.FLAG_ACTIVITY_MULTIPLE_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.multipleTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NEW_TASK] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.newTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NO_ANIMATION] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.noAnimation(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) }

/**
 * Add the [Intent.FLAG_ACTIVITY_NO_HISTORY] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.noHistory(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY) }

/**
 * Add the [Intent.FLAG_ACTIVITY_SINGLE_TOP] flag to the [Intent].
 *
 * @return the same intent with the flag applied.
 */
inline fun Intent.singleTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) }

object IntentUtil {

    @JvmStatic
    fun <T> createIntent(ctx: Context, clazz: Class<out T>, params: Array<out Pair<String, Any?>>): Intent {
        val intent = Intent(ctx, clazz)
        if (params.isNotEmpty()) fillIntentArguments(intent, params)
        return intent
    }

    @JvmStatic
    fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any?>>) {
        params.forEach {
            val value = it.second
            when (value) {
                null -> {
                }
                is Int -> intent.putExtra(it.first, value)
                is Long -> intent.putExtra(it.first, value)
                is CharSequence -> intent.putExtra(it.first, value)
                is String -> intent.putExtra(it.first, value)
                is Float -> intent.putExtra(it.first, value)
                is Double -> intent.putExtra(it.first, value)
                is Char -> intent.putExtra(it.first, value)
                is Short -> intent.putExtra(it.first, value)
                is Boolean -> intent.putExtra(it.first, value)
                is java.io.Serializable -> intent.putExtra(it.first, value)
                is Bundle -> intent.putExtra(it.first, value)
                is Parcelable -> intent.putExtra(it.first, value)
                is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
                    value.isArrayOf<String>() -> intent.putExtra(it.first, value)
                    value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
                    else -> throw Exception("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
                }
                is IntArray -> intent.putExtra(it.first, value)
                is LongArray -> intent.putExtra(it.first, value)
                is FloatArray -> intent.putExtra(it.first, value)
                is DoubleArray -> intent.putExtra(it.first, value)
                is CharArray -> intent.putExtra(it.first, value)
                is ShortArray -> intent.putExtra(it.first, value)
                is BooleanArray -> intent.putExtra(it.first, value)
                else -> throw Exception("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            return@forEach
        }
    }

    @JvmStatic
    fun fillBundleArguments(bundle: Bundle, params: Array<out Pair<String, Any?>>) {
        params.forEach {
            val value = it.second
            when (value) {
                null -> {
                }
                is Int -> bundle.putInt(it.first, value)
                is Long -> bundle.putLong(it.first, value)
                is CharSequence -> bundle.putCharSequence(it.first, value)
                is String -> bundle.putString(it.first, value)
                is Float -> bundle.putFloat(it.first, value)
                is Double -> bundle.putDouble(it.first, value)
                is Char -> bundle.putChar(it.first, value)
                is Short -> bundle.putShort(it.first, value)
                is Boolean -> bundle.putBoolean(it.first, value)
                is java.io.Serializable -> bundle.putSerializable(it.first, value)
                is Bundle -> bundle.putBundle(it.first, value)
                is Parcelable -> bundle.putParcelable(it.first, value)
                is IntArray -> bundle.putIntArray(it.first, value)
                is LongArray -> bundle.putLongArray(it.first, value)
                is FloatArray -> bundle.putFloatArray(it.first, value)
                is DoubleArray -> bundle.putDoubleArray(it.first, value)
                is CharArray -> bundle.putCharArray(it.first, value)
                is ShortArray -> bundle.putShortArray(it.first, value)
                is BooleanArray -> bundle.putBooleanArray(it.first, value)
                else -> bundle.putSerializable(it.first, value as java.io.Serializable)
            }
            return@forEach
        }
    }

    @JvmStatic
    fun internalStartActivityForResult(
            act: Activity,
            activity: Class<out Activity>,
            requestCode: Int,
            params: Array<out Pair<String, Any?>>,
            enableAnimation: Boolean
    ) {
//        if (enableAnimation&&AppUtil.sdkVersion>23) {
//            act.startActivityForResult(createIntent(act, activity, params), requestCode, ActivityOptions.makeSceneTransitionAnimation(act).toBundle())
//        } else {
        act.startActivityForResult(createIntent(act, activity, params), requestCode)
//        }
    }
}