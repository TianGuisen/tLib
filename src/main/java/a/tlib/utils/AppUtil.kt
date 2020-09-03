package a.tlib.utils

import a.tlib.LibApp
import android.app.Activity
import android.app.ActivityManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager


object AppUtil {
    var sdkVersion = Build.VERSION.SDK_INT

    /**
     * 网络是否链接
     */
    @JvmStatic
    fun isConnected(): Boolean {
        (LibApp.app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo.also {
            if (null != it && it.isConnected) {
                if (it.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        }
        return false
    }

    fun getProcessName(context: Context?): String? {
        if (context == null) return null
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == Process.myPid()) {
                return processInfo.processName
            }
        }
        return null
    }

    /**
     * 设备id
     */
    val deviceId: String = Settings.Secure.getString(LibApp.app.contentResolver, Settings.Secure.ANDROID_ID)

    /**
     * 拨打电话,需要权限
     */
    @JvmStatic
    fun callPhone(num: String) {
        val intent = Intent(Intent.ACTION_CALL)
        val data = Uri.parse("tel:${num}")
        intent.data = data
        LibApp.app.startActivity(intent)
    }

    /**
     * 获取状态栏高度
     */
    @JvmStatic
    fun getStateBarHeight(): Int {
        var result = 0
        val resourceId: Int = LibApp.app.getResources().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = LibApp.app.getResources().getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 获取屏幕高
     */
    @JvmStatic
    fun getScreenHeight(): Int {
        val metric = DisplayMetrics()
        val wm = LibApp.app.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metric)
        return metric.heightPixels
    }

    /**
     * 获取屏幕宽
     */
    @JvmStatic
    fun getScreenWidth(): Int {
        val metric = DisplayMetrics()
        val wm = LibApp.app.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metric)
        return metric.widthPixels
    }

    /**
     * 隐藏键盘
     */
    @JvmStatic
    fun hideSoftKeyBoard(activity: Activity) {
        val localView = activity.currentFocus
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (localView != null && imm != null) {
            imm.hideSoftInputFromWindow(localView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /**
     * 设置屏幕常亮
     */
    @JvmStatic
    fun setScreenBright(act: Activity) {
        act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /**
     * 是否是横屏
     */
    @JvmStatic
    fun isHorizontalScreen(): Boolean {
        val ori = LibApp.app.resources.configuration.orientation
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            return true
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            return false
        }
        return false
    }


    /**
     * 复制到剪切板
     */
    @JvmStatic
    fun setCopy(infoStr: String?) {
        if (infoStr.isNotNullEmpty()) {
            val cm = LibApp.app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = ClipData.newPlainText("Label", infoStr)
            cm.setPrimaryClip(mClipData)
        }
    }
}
