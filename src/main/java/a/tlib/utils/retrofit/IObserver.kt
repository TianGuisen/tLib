package com.lb.baselib.retrofit

import a.tlib.utils.ToastUtil
import a.tlib.utils.retrofit.LoadView
import a.tlib.utils.retrofit.ProgressDiaUtil
import android.content.Context
import android.os.Looper
import com.jeremyliao.liveeventbus.LiveEventBus
import io.reactivex.disposables.Disposable

interface IObserver<T> {
    var lv: LoadView?
    var context: Context?
    var tag: String?
    var repeat: Int
    var showToast: Boolean

    fun onSucces(t: ResWrapper<T>)

    fun onFailure(t: ResWrapper<T>?) {

    }

    /**
     * 不管成功或失败都会调用
     */
    fun onFinish() {

    }

    fun startHandle(d: Disposable) {
        if (repeat == 1) {
            ApiTagManager.instance.add1(tag, d)
        } else if (repeat == 2) {
            ApiTagManager.instance.add2(tag, d)
        }
        context?.let {
            ProgressDiaUtil.show(it)
        }
    }

    fun finishHandle() {
        if (repeat != 0) {
            ApiTagManager.instance.remove(tag)
        }
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

    fun isSuccess(t: ResWrapper<T>): Boolean {
        return t.code == ResCode.RESPONSE_SUCCESS2|| t.code == ResCode.RESPONSE_SUCCESS
    }

    /**
     * 检查登录
     */
    fun checkLogin(t: ResWrapper<T>): Boolean {
        if (t.code == ResCode.TOKEN_OVERDUE.toInt()) {
            //跳转到登陆界面
//            LiveEventBus.get().with("" + ResCode.TOKEN_OVERDUE).post(null);
            LiveEventBus.get(ResCode.TOKEN_OVERDUE).post(null)
            return false
        }
        return true
    }
}
