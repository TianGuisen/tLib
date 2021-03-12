package a.tlib.utils

import a.tlib.LibApp
import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager


object AppUtil {
    var sdkVersion = Build.VERSION.SDK_INT

    /**
     * 获取版本号
     */
    fun getVersionName(): String {
        val manager: PackageManager = LibApp.app.getPackageManager()
        val info: PackageInfo = manager.getPackageInfo(LibApp.app.getPackageName(), 0)
        return info.versionName
    }
    /**
     * 获取版本号
     */
    fun getVersionCode(): Int {
        val manager: PackageManager = LibApp.app.getPackageManager()
        val info: PackageInfo = manager.getPackageInfo(LibApp.app.getPackageName(), 0)
        return info.versionCode
    }
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

    /**
     * 获取当前的网络状态 ：没有网络-0：WIFI网络1：4G网络-4：3G网络-3：2G网络-2
     * 自定义
     *
     * @param context
     * @return
     */
    @JvmStatic
    fun getNetType(context: Context): Int {
        //结果返回值
        var netType = 0
        //获取手机所有连接管理对象
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //获取NetworkInfo对象
        val networkInfo = manager.activeNetworkInfo ?: return netType
        //NetworkInfo对象为空 则代表没有网络
        //否则 NetworkInfo对象不为空 则获取该networkInfo的类型
        val nType = networkInfo.type
        if (nType == ConnectivityManager.TYPE_WIFI) {
            //WIFI
            netType = 1
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            val nSubType = networkInfo.subtype
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            //3G   联通的3G为UMTS或HSDPA 电信的3G为EVDO
            netType = if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && !telephonyManager.isNetworkRoaming) {
                4
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA || (nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                            && !telephonyManager.isNetworkRoaming)) {
                3
                //2G 移动和联通的2G为GPRS或EGDE，电信的2G为CDMA
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS || nSubType == TelephonyManager.NETWORK_TYPE_EDGE || (nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                            && !telephonyManager.isNetworkRoaming)) {
                2
            } else {
                2
            }
        }
        return netType
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

    /**
     * 获取android7.0以上需要的授权
     */
    @JvmStatic
    fun getAuthorities(act: Activity): String {
        return act.getPackageName() + ".fileProvider"
    }


    //当本应用位于后台时，则将它切换到最前端
    fun setTopApp(context: Context) {
        if (isRunningForeground(context)) {
            return
        }
        //获取ActivityManager
        val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager

        //获得当前运行的task(任务)
        val taskInfoList = activityManager.getRunningTasks(100)
        for (taskInfo in taskInfoList) {
            //找到本应用的 task，并将它切换到前台
            if (taskInfo.topActivity!!.packageName == context.packageName) {
                activityManager.moveTaskToFront(taskInfo.id, 0)
                return 
            }
        }
    }

    //判断本应用是否已经位于最前端：已经位于最前端时，返回 true；否则返回 false
    fun isRunningForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val appProcessInfoList = activityManager.runningAppProcesses
        for (appProcessInfo in appProcessInfoList) {
            if (appProcessInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcessInfo.processName == context.applicationInfo.processName) {
                return true
            }
        }
        return false
    }
}
