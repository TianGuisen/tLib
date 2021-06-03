package a.tlib

import a.tlib.utils.ActStackManager
import a.tlib.utils.AppUtil
import a.tlib.utils.CrashCollectHandler
import a.tlib.utils.ToastUtil
import a.tlib.widget.TitleBar
import android.app.Application
import android.content.Context
import android.os.Build
import android.webkit.WebView
import a.tlib.utils.retrofit.RetrofitService
import com.orhanobut.logger.*
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.tencent.smtt.sdk.QbSdk
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

    /**
     * APP标记，用于标记不同的app
     * 0是旧有播
     * 1是新有播
     */
    @JvmStatic
    var appSign = 0

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
            initX5()
            if (BuildConfig.IS_DEBUG) CrashCollectHandler().init()//debug时候错误捕获禁止重启
            RxJavaPlugins.setErrorHandler {
                //网络异常线上可能会崩溃，需要这个
            }
        }
        return this
    }

    /**
     * 设置默认标题风格
     */
    @JvmStatic
    fun setDefaultTitleStyle(style:Int) {
        TitleBar.defaultStype=style
    }

    /**
     * 添加其他请求头
     */
    @JvmStatic
    fun addHeaderMap(vararg params: Pair<String, String>): LibApp {
        RetrofitService.headerMap = mutableMapOf()
        RetrofitService.headerMap!!.putAll(params)
        return this
    }

    @JvmStatic
    private fun initX5() {
        //腾讯x5
        QbSdk.initX5Environment(app, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {

            }

            override fun onViewInitFinished(p0: Boolean) {

            }

        })
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
                .setSupportSP(false).supportSubunits = Subunits.PT
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