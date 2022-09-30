package a.tlib.utils

import android.os.CountDownTimer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * @author 田桂森 2021/6/29 0029
 * 通用的倒计时，在activity和fragment中使用，已经自动绑定生命周期，在页面销毁的时候关闭
 */
class TCountDownTimer private constructor(var totalTime: Long, var interval: Long) : CountDownTimer(totalTime, interval), DefaultLifecycleObserver {
    companion object {
        /**
         * @totalTime 总时间长度，单位毫秒，默认无限时间
         * @interval 计时间隔，单位毫秒，默认1秒
         */
        @JvmStatic
        fun newInstance(life: LifecycleOwner, totalTime: Long = Long.MAX_VALUE, interval: Long = 1000): TCountDownTimer {
            val tCountDownTimer = TCountDownTimer(totalTime, interval)
            life.getLifecycle().addObserver(tCountDownTimer)
            return tCountDownTimer
        }
    }

    /**
     * 倒计时监听
     */
    var countDownLis: ((Long) -> Unit)? = null

    /**
     * 结束
     */
    var finishLis: (() -> Unit)? = null

    /**
     * 正计时监听
     */
    var countLis: ((Long) -> Unit)? = null

    /**
     * 正计时
     */
    var count = 0L

    /**
     * 次数统计，onTick每执行一次，+1
     */
    var absoluteCount=0
    
    override fun onTick(millisUntilFinished: Long) {
        absoluteCount++
        count += interval
        countLis?.invoke(count)
        countDownLis?.invoke(millisUntilFinished)
    }

    override fun onFinish() {
        finishLis?.invoke()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        cancel()
        super.onDestroy(owner)
    }
}