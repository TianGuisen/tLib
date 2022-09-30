package a.tlib

import a.tlib.logger.*
import a.tlib.utils.ActStackManager
import a.tlib.utils.AppUtil
import a.tlib.utils.DateUtil
import a.tlib.utils.ToastUtil
import a.tlib.utils.retrofit.LoadView
import a.tlib.widget.TitleBar
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.webkit.WebView
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.androidisland.vita.startVita
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator
import io.reactivex.plugins.RxJavaPlugins
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.unit.Subunits


/**
 * @author 田桂森 2020/8/14 0014
 */
object LibApp {
    /**
     * Logger日志的tag
     */
    const val LOGGER_TAG = "logtag"
    lateinit var app: Application
    
    

    @JvmStatic
    fun init(appContext: Application): LibApp {
        appContext.apply {
            app = this
            ActStackManager.register(this)
            initLog()
            ToastUtil.init(this)
            fixWebView()
            initAutoSize()
            initRefresh()
            hidePAPIDialog()///解决9.0问题弹窗
//            if (BuildConfig.IS_DEBUG) CrashCollectHandler().init()//debug时候错误捕获禁止重启
            RxJavaPlugins.setErrorHandler {
                //网络异常线上可能会崩溃，需要这个
            }
            //测试包时候崩溃弹出错误页面
            CaocConfig.Builder.create().enabled(BuildConfig.IS_DEBUG).apply()
            startVita()//viewmodel
        }
        return this
    }

    /**
     * 设置LoadView的默认布局
     */
    @JvmStatic
    fun setDefaultLoadViewLayoutId(emptyLayoutId: Int, errorLayoutId: Int, loadingLayoutId: Int, noNetworkLayoutId: Int, loginLayoutId: Int): LibApp {
        LoadView.defaultEmptyLayoutId = emptyLayoutId
        LoadView.defaultErrorLayoutId = errorLayoutId
        LoadView.defaultLoadingLayoutId = loadingLayoutId
        LoadView.defaultNoNetworkLayoutId = noNetworkLayoutId
        LoadView.defaultLoginLayoutId = loginLayoutId
        return this
    }


    @JvmStatic
    private fun initLog() {
        val formatStrategy = TFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(1)
                .methodOffset(0)
                .tag(LOGGER_TAG)
                .build()
        YLog2.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.IS_DEBUG
            }
        })
        val formatStrategy2 = TFormatStrategy2.newBuilder()
                .showThreadInfo(false)
                .methodCount(1)
                .methodOffset(0)
                .tag(LOGGER_TAG)
                .build()
        YLog.addLogAdapter(object : AndroidLogAdapter(formatStrategy2) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.IS_DEBUG
            }
        })
    }

    @JvmStatic
    private fun initAutoSize() {
        AutoSizeConfig.getInstance()
                .setCustomFragment(true)
//                .setBaseOnWidth(false)//以高度适配
                .setUseDeviceSize(true)
                .unitsManager
                .setSupportDP(false)
                .setSupportSP(false)
//                .supportSubunits = Subunits.PT//以PT为单位
    }

    @JvmStatic
    private fun initRefresh() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            MaterialHeader(context)
                    .setColorSchemeResources(R.color.swiperefesh_color_one, R.color.swiperefesh_color_two)//全局设置主题颜色,这个有效
//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(object : DefaultRefreshFooterCreator {
            override fun createRefreshFooter(context: Context, layout: RefreshLayout): RefreshFooter {
                //指定为经典Footer，默认是 BallPulseFooter
                return ClassicsFooter(context).setDrawableSize(20f)
            }
        })
    }


    /**
     * 隐藏9.0启屏弹窗
     */
    @SuppressLint("SoonBlockedPrivateApi")
    @JvmStatic
    private fun hidePAPIDialog() {
        try {
            val cls = Class.forName("android.app.ActivityThread")
            val declaredMethod = cls.getDeclaredMethod("currentActivityThread")
            declaredMethod.isAccessible = true
            val activityThread = declaredMethod.invoke(null)
            val mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown")
            mHiddenApiWarningShown.isAccessible = true
            mHiddenApiWarningShown.setBoolean(activityThread, true)
        } catch (e: Exception) {

        }
    }

    /**
     *  //Android P 以及之后版本不支持同时从多个进程使用具有相同数据目录的WebView
     *  https://blog.csdn.net/W_LIN/article/details/103011926/
     */
    @JvmStatic
    private fun fixWebView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = AppUtil.getProcessName(app)
            if ("com.youbo.video" != processName && "com.haibaoshow.youbo" != processName) { //判断不等于默认进程名称
                processName?.let { WebView.setDataDirectorySuffix(it) }
            }
        }
    }

}