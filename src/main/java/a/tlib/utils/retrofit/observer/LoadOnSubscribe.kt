package a.tlib.utils.retrofit.observer

import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.atomic.AtomicLong

/**
 * @author 田桂森 2021/3/22 0022
 */


/**
 * 文件上传，下载 进度观察者 发射器（计算上传百分比）
 * Created by fangs on 2018/5/21.
Observable.merge(Observable.create(loadOnSubscribe), newApi.uploadSingleImage2(params))
.subscribeOn(Schedulers.io())
.observeOn(AndroidSchedulers.mainThread())
.subscribe(object : ProgressReqObserver<Any>() {
override fun onProgress(percent: String) {
}

override fun onSuccess(t: Any) {

}
})
 */
class LoadOnSubscribe : ObservableOnSubscribe<Any> {
    private var mObservableEmitter //进度观察者 发射器
            : ObservableEmitter<Any>? = null
    var mSumLength = 0L //总长度
    var uploaded = AtomicLong() //已经上传 长度
    private var mPercent = 0.0 //已经上传进度 百分比


    override fun subscribe(emitter: ObservableEmitter<Any>) {
        mObservableEmitter = emitter
    }

    fun setmSumLength(mSumLength: Long) {
        this.mSumLength = mSumLength
    }

    fun onRead(read: Long) {
        uploaded.addAndGet(read)
        if (mSumLength <= 0) {
            onProgress(0.0)
        } else {
            onProgress(100.0 * uploaded.get() / mSumLength)
        }
    }

    private fun onProgress(percent: Double) {
        if (null == mObservableEmitter) return
        if (percent == mPercent) return
        mPercent = percent
        mObservableEmitter!!.onNext(percent)
    }

    //上传完成 清理进度数据
    fun clean() {
        mPercent = 0.0
        uploaded = AtomicLong()
        mSumLength = 0L
        mObservableEmitter!!.onComplete()
    }

}