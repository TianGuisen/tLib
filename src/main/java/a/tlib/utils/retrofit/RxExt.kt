package a.tlib.utils.retrofit

//import com.lb.baselib.retrofit.ZipObserver
import android.content.Context
import com.lb.baselib.retrofit.NormalObserver
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.uber.autodispose.SingleSubscribeProxy
import io.reactivex.Single

/**
 * @author 田桂森 2019/6/26
 */
/**
 * 普通请求
 */
fun <T> Single<ResWrapper<T>>.normalSub(
        onSuccess: ((ResWrapper<T>) -> Unit)? = null,
        onFailure: ((ResWrapper<T>?) -> Unit)? = null,
        onFinish: (() -> Unit)? = null,
        context: Context? = null,//传入表示显示dialog
        lv: LoadView? = null,//传入就显示loadView
        srl: SmartRefreshLayout? = null,//传入就自动在请求完成后隐藏加载动画
        showToast: Boolean = true,//默认显示错误toast
        jumpLogin: Boolean = false//code返回需要登录的时候，是否自动跳转到登录页面，通常列表接口不跳转，其他接口跳转
) {
    val ob = object : NormalObserver<T>(context, lv, srl, showToast, jumpLogin) {
        override fun onSucces(t: ResWrapper<T>) {
            onSuccess?.let { onSuccess(t) }
        }

        override fun onFailure(t: ResWrapper<T>?) {
            onFailure?.let { onFailure(t) }
        }

        override fun onFinish() {
            onFinish?.let { onFinish() }
        }
    }
    subscribe(ob)
}

/**
 * 普通请求
 */
fun <T> SingleSubscribeProxy<ResWrapper<T>>.normalSub(
        onSuccess: ((ResWrapper<T>) -> Unit)? = null,
        onFailure: ((ResWrapper<T>?) -> Unit)? = null,
        onFinish: (() -> Unit)? = null,
        context: Context? = null,//传入表示显示dialog
        lv: LoadView? = null,//传入就显示loadView
        srl: SmartRefreshLayout? = null,//传入就自动在请求完成后隐藏加载动画
        showToast: Boolean = true,//默认显示错误toast
        jumpLogin: Boolean = false//code返回需要登录的时候，是否自动跳转到登录页面，通常列表接口不跳转，其他接口跳转
) {
    val ob = object : NormalObserver<T>(context, lv, srl, showToast, jumpLogin) {
        override fun onSucces(t: ResWrapper<T>) {
            onSuccess?.let { onSuccess(t) }
        }

        override fun onFailure(t: ResWrapper<T>?) {
            onFailure?.let { onFailure(t) }
        }

        override fun onFinish() {
            onFinish?.let { onFinish() }
        }
    }
    subscribe(ob)
}

///**
// * 并行请求
// */
//fun Single<MutableList<Any>>.zipSub(
//        onSuccess: ((MutableList<Any>) -> Unit)? = null,
//        onFailure: ((ResWrapper<Any>?) -> Unit)? = null,
//        onFinish: (() -> Unit)? = null
//) {
//    val ob = object : ZipObserver<Any>() {
//        override fun onSucces(data: MutableList<Any>) {
//            onSuccess?.let { onSuccess(data) }
//        }
//
//        override fun onFailure(t: ResWrapper<Any>?) {
//            onFailure?.let { onFailure(t) }
//        }
//
//        override fun onFinish() {
//            onFinish?.let { onFinish() }
//        }
//    }
//    subscribe(ob)
//}