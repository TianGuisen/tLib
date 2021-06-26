package com.lb.baselib.retrofit

import Interceptors
import a.tlib.logger.YLog
import a.tlib.utils.ToastUtil
import a.tlib.utils.retrofit.ApiErrorType
import a.tlib.utils.retrofit.LoadView
import a.tlib.utils.retrofit.ResWrapper
import android.content.Context
import android.os.Looper
import a.tlib.logger.YLog2
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 网络请回调处理
 */
abstract class NormalObserver<T> : SingleObserver<ResWrapper<T>>, IObserver<T> {
    override var lv: LoadView? = null
    override var context: Context? = null
    override var tag: String? = null
    override var showToast: Boolean = true
    override var jumpLogin: Boolean = false
    var srl: SmartRefreshLayout? = null

    /**
     *@loading 是否显示loading
     *@showToast 是否显示错误toast
     */
    constructor(context: Context? = null, lv: LoadView? = null, srl: SmartRefreshLayout? = null, showToast: Boolean = true,
                jumpLogin: Boolean = false) {
        this.context = context
        this.srl = srl
        this.lv = lv
        this.showToast = showToast
        this.jumpLogin = jumpLogin
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
                else -> {
                    e.message?.let {
                        YLog2.t(Interceptors.LOGGER_NET_TAG).e(it, e)
                    }
                    null
                }
            }
        }
        if (showToast) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                ToastUtil.normal(apiError?.message)
            }
        }
        srl?.finishRefresh(false)
        srl?.finishLoadMore(false)

        lv?.showError()
        e.message?.let { YLog2.t(Interceptors.LOGGER_NET_TAG).e(it, e) }
        onFailure(null)
        finishHandle()
        onFinish()
    }

    override fun onSuccess(t: ResWrapper<T>) {
        try {//主要捕获在onSucces()的回调里出现异常
            if (!checkLogin(t)) {
                onFailure(t)
                lv?.showLogin()
            } else if (isSuccess(t)) {
                srl?.finishRefresh(true)
                lv?.showContent()
                onSucces(t)
            } else {
                showErrorToast(t)
                srl?.finishRefresh(false)
                lv?.showError()
                onFailure(t)
            }
        } catch (e: Exception) {
            e.message?.let {
                YLog2.t(Interceptors.LOGGER_NET_TAG).e(it, e)
            }
        } finally {
            srl?.finishLoadMore(true)
            finishHandle()
            onFinish()
        }

    }
}