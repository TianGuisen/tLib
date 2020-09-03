//package com.lb.baselib.retrofit
//
//import android.content.Context
//import android.os.Looper
//import com.haibaoshow.youbo.consts.SDKKeyConfig
//import a.tlib.utils.retrofit.LoadView
//import a.tlib.utils.ToastUtil
//import com.orhanobut.logger.YLog2
//import com.scwang.smartrefresh.layout.SmartRefreshLayout
//import io.reactivex.SingleObserver
//import io.reactivex.disposables.Disposable
//import retrofit2.HttpException
//import java.net.ConnectException
//import java.net.SocketTimeoutException
//import java.net.UnknownHostException
//
///**
// * recyclerview的网络请回调处理
// */
//abstract class RVObserver<T> : SingleObserver<ResWrapper<T>>, IObserver<T> {
//    override var lv: LoadView? = null
//    override var context: Context? = null
//    override var tag: String? = null
//    override var showToast: Boolean = true
//    override var repeat = 0
//    var srl: SmartRefreshLayout? = null
//    var page: Int = 0
//    var isFirstLoad = true
//
//    /**
//     *@loading RecyclerView的LoadView
//     *@page:页码
//     *@tag: 请求标记,传入url,
//     *@repeat 重复请求策略,默认0,1和2时必须要传入tag
//     *        0:允许重复请求,场景:viewpager多个页面的请求一样但是参数不同,可能短时间内请求多个页面数据
//     *        1:关闭后入队的请求,比较常用:比如按钮的防重复时间是500,但是点击按钮后后台处理时间长达1000,在后500时间内按钮是可点击的但是请求是无意义的
//     *        2:关闭先入队的请求,场景很少:频繁调用接口并只以最后一次的数据为准,出现这种情况通常设计不合理
//     */
//    constructor(loadView: LoadView? = null, srl: SmartRefreshLayout? = null, page: Int = 1, showToast: Boolean = true, tag: String? = null, repeat: Int = 0) {
//        this.lv = loadView
//        this.srl = srl
//        this.page = page
//        this.tag = tag
//        this.showToast = showToast
//        this.repeat = repeat
//    }
//
//    constructor(loadView: LoadView? = null, page: Int = 1, showToast: Boolean = true) {
//        this.lv = loadView
//        this.page = page
//        this.showToast = showToast
//    }
//
//    override fun onSubscribe(d: Disposable) {
//        srl?.resetNoMoreData()
//        startHandle(d)
//    }
//
//    final override fun onError(e: Throwable) {
//        val apiError = if (e is HttpException) { //连接成功但后台返回错误状态码
//            if (page <= 1) {
//                lv?.showError()
//            }
//            when (e.code()) {
//                ApiErrorType.INTERNAL_SERVER_ERROR.code ->
//                    ApiErrorType.INTERNAL_SERVER_ERROR
//                ApiErrorType.BAD_GATEWAY.code ->
//                    ApiErrorType.BAD_GATEWAY
//                ApiErrorType.NOT_FOUND.code ->
//                    ApiErrorType.NOT_FOUND
//                ApiErrorType.GATEWAY_TIMEOUT.code ->
//                    ApiErrorType.GATEWAY_TIMEOUT
//                else -> ApiErrorType.UNEXPECTED_ERROR
//            }
//        } else {
//            if (page <= 1) {
//                lv?.showNoNetwork()
//            }
//            when (e) {//发送网络问题或其它未知问题
//                is UnknownHostException -> ApiErrorType.NETWORK_NOT_CONNECT
//                is ConnectException -> ApiErrorType.NETWORK_NOT_CONNECT
//                is SocketTimeoutException -> ApiErrorType.CONNECTION_TIMEOUT
//                else -> {
//                    e.message?.let {
//                        YLog2.t(SDKKeyConfig.LOGGER_NET_TAG).e(it)
//                    }
//                    null
//                }
//            }
//        }
//        if (showToast) {
//            if (Looper.myLooper() == Looper.getMainLooper()) {
//                ToastUtil.normal(apiError?.message)
//            }
//        }
//        e.message?.let { YLog2.t(SDKKeyConfig.LOGGER_NET_TAG).e(it, e) }
//        hideSrl(false,null)
//        onFailure(null)
//        finishHandle()
//        onFinish()
//    }
//
//    override fun onSuccess(t: ResWrapper<T>) {
//        if (!checkLogin(t)) {
//            lv?.showLogin()
//        } else if (isSuccess(t)) {
//            showLoadView(t.data)
//            hideSrl(true, t.data)
//            onSucces(t)
//        } else {
//            hideSrl(false, t.data)
//            lv?.showError()
//            showErrorToast(t)
//            onFailure(t)
//        }
//        finishHandle()
//        onFinish()
//    }
//
//    private fun hideSrl(b: Boolean, data: T?) {
//        if (Looper.myLooper() == Looper.getMainLooper()) {
//            if (page == 1) {
//                srl?.finishRefresh(b)
//            } else {
//                if (data is List<*> && data.isEmpty()) {
//                    srl?.setNoMoreData(false)
//                }
//                srl?.finishLoadMore(b)
//            }
//        }
//    }
//
//    private fun showLoadView(data: T?) {
//        if (data is List<*>) {
//            if (data.isEmpty()) {
//                if (page <= 1) {
//                    lv?.showEmpty()
//                } else {
//                    // ToastUtil.normal("没有更多数据!")
//                }
//            } else {
//                lv?.showContent()
//            }
//        } else {
//            lv?.showEmpty()
//        }
//    }
//
//}