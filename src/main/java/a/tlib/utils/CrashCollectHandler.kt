package a.tlib.utils

import a.tlib.LibApp
import android.os.Looper

/**
 * @author 田桂森 2020/2/11
 */
class CrashCollectHandler : Thread.UncaughtExceptionHandler {
    var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    fun init() {
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    //当UncaughtException发生时会转入该函数来处理
    override fun uncaughtException(t: Thread?, e: Throwable?) {
        if (!handleException(e) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler?.uncaughtException(t, e)
        } else {
            try {
                //给Toast留出时间
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            //退出程序
            ActStackManager.finishAllActivity()
            ActStackManager.unRegister(LibApp.app)
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)

        }

    }

    fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }
        Thread {
            Looper.prepare()
            ToastUtil.error("很抱歉,程序出现异常,即将退出")
            Looper.loop()
        }.start()
        return true
    }
}