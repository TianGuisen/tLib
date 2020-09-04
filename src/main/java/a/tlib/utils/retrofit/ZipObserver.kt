package com.lb.baselib.retrofit

import Interceptors
import a.tlib.utils.ToastUtil
import a.tlib.utils.retrofit.LoadView
import android.content.Context
import android.os.Looper
import com.orhanobut.logger.YLog2
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 网络请回调处理zip
 */
abstract class ZipObserver<T> : SingleObserver<MutableList<T>>, IObserver<T> {
    override var context: Context? = null
    override var lv: LoadView? = null
    override var tag: String? = null
    override var showToast: Boolean = true
    override var repeat = 0

    /**
     *@loading 是否显示loading
     *@showToast 是否显示错误toast
     *@tag: 请求标记,传入url
     *@repeat 重复请求策略,默认0,1和2时必须要传入tag
     *        0:允许重复请求,场景:viewpager多个页面的请求一样但是参数不同,可能短时间内请求多个页面数据
     *        1:关闭后入队的请求,比较常用:比如按钮的防重复时间是500,但是点击按钮后后台处理时间长达1000,在后500时间内按钮是可点击的但是请求是无意义的
     *        2:关闭先入队的请求,场景很少:频繁调用接口并只以最后一次的数据为准,出现这种情况通常设计不合理
     */
    constructor(context: Context?,lv: LoadView?, showToast: Boolean = true, tag: String?, repeat: Int = 0) {
        this.tag = tag
        this.lv=lv
        this.context = context
        this.showToast = showToast
        this.repeat = repeat
    }

    constructor(showToast: Boolean = true) {
        this.showToast = showToast
    }


    override fun onSubscribe(d: Disposable) {
        startHandle(d)
    }

    final override fun onError(e: Throwable) {
        val apiError = if (e is HttpException) { //连接成功但后台返回错误状态码
            when (e.code()) {
                ApiErrorType.INTERNAL_SERVER_ERROR.code ->
                    ApiErrorType.INTERNAL_SERVER_ERROR
                ApiErrorType.BAD_GATEWAY.code ->
                    ApiErrorType.BAD_GATEWAY
                ApiErrorType.NOT_FOUND.code ->
                    ApiErrorType.NOT_FOUND
                else -> ApiErrorType.UNEXPECTED_ERROR
            }
        } else {
            when (e) {//发送网络问题或其它未知问题
                is UnknownHostException -> ApiErrorType.UNKNOWN_HOST
                is ConnectException -> ApiErrorType.NETWORK_NOT_CONNECT
                is SocketTimeoutException -> ApiErrorType.CONNECTION_TIMEOUT
                else -> ApiErrorType.UNEXPECTED_ERROR
            }
        }

        if (showToast) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                ToastUtil.normal(apiError.message)
            }
        }
        lv?.showError()
        e.message?.let { YLog2.t(Interceptors.LOGGER_NET_TAG).e(it, e) }
        onFailure(null)
        finishHandle()
        onFinish()
    }

    override fun onSuccess(t: MutableList<T>) {
        lv?.showContent()
        onSucces(t)
        finishHandle()
        onFinish()
    }

    /**
     * 类型不适用IObserver的所以..
     */
    abstract fun onSucces(data: MutableList<T>)
    override fun onSucces(t: ResWrapper<T>) {

    }
}