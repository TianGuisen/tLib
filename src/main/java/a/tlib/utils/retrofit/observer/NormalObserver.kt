package com.lb.baselib.retrofit

import a.tlib.utils.retrofit.ApiErrorType
import a.tlib.utils.retrofit.LoadView
import a.tlib.utils.retrofit.ResWrapper
import a.tlib.logger.YLog2
import a.tlib.utils.DateUtil
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import io.reactivex.SingleObserver
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import a.tlib.utils.ProgressDiaUtil
import a.tlib.utils.ToastUtil
import a.tlib.utils.retrofit.*
import android.content.Context
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jeremyliao.liveeventbus.LiveEventBus
import io.reactivex.disposables.Disposable

/**
 * 网络请回调处理
 */
abstract class NormalObserver<T> : SingleObserver<ResWrapper<T>> {
    var lv: LoadView? = null
    var context: Context? = null
    var tag: String? = null
    var showToast: Boolean = true
    var jumpLogin: Boolean = false
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
//                    e.message?.let {
//                        YLog2.t(Interceptors.LOGGER_NET_TAG).e(it, e)
//                    }
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
        try {//主要捕获在onSucces()
            if (t.timestamp > 0) {
                DateUtil.lastServerTime = t.timestamp
            }
            if (!checkLogin(t)) {
                onFailure(t)
                lv?.showLogin()
            } else if (t.isSuccess()) {
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
            if (e.message != null) {
                YLog2.t(Interceptors.LOGGER_NET_TAG).e(e.message!!, e)
            } else {
                YLog2.t(Interceptors.LOGGER_NET_TAG).e(e.toString(), e)
            }
        } finally {
            srl?.finishLoadMore(true)
            finishHandle()
            onFinish()
        }
    }

    abstract fun onSucces(t: ResWrapper<T>)

    open fun onFailure(t: ResWrapper<T>?) {

    }

    /**
     * 不管成功或失败都会调用
     */
    open fun onFinish() {

    }

    fun startHandle(d: Disposable) {
        context?.let {
            if (it is FragmentActivity) {
                ProgressDiaUtil.show(it)
            } else if (context is Fragment) {
                ProgressDiaUtil.show(context as Fragment)
            } else {
                ProgressDiaUtil.show(it)
            }
        }
    }

    fun finishHandle() {
        context?.let {
            ProgressDiaUtil.dismiss()
        }
    }

    fun showErrorToast(t: ResWrapper<T>) {
        if (showToast) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                ToastUtil.normal(t.message)
            }
        }
    }

    /**
     * 检查登录
     */
    fun checkLogin(t: ResWrapper<T>): Boolean {
        if (t.notLogin()) {
            if (jumpLogin) {
                //跳转到登陆界面
                LiveEventBus.get(ResCode.TOKEN_OVERDUE.toString()).post(null)
            }
            return false
        }
        return true
    }
}