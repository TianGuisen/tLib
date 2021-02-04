package a.tlib.base

import a.tlib.R
import a.tlib.utils.gson.GsonUtil
import a.tlib.widget.TitleBar
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
import a.tlib.utils.StringUtils
import a.tlib.utils.getcolor
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeCompat
import me.jessyan.autosize.AutoSizeConfig


/**
 * @version:      由有播科技（杭州）有限公司开发
 * @date:         2018/10/17  16:54 , @author:  qaufu
 */
abstract class BaseActivity : AppCompatActivity(), View.OnClickListener {
    val TAG = this.javaClass.simpleName
    var gson = GsonUtil.gson
    var strUtils = StringUtils()
    abstract val layoutId: Int
    var titleBar: TitleBar? = null
    lateinit var act: BaseActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = this
        WebView.enableSlowWholeDocumentDraw()
        setContentView(layoutId)
        initImmersionBar()
        initView()
    }

    /**
     * 沉淀式状态栏字体颜色控制，默认情况下是黑底，在修妖修改的界面可以重写此方法，然后把状态栏的底色传进来即可
     */
    open fun initImmersionBar(color: Int = R.color.status_bar_color_transparent_black) {
        titleBar = findViewById(setTitleBar())
        titleBar?.let {
            ImmersionBar.setTitleBar(this, it)
            it.setOnLeftImageListener {
                leftClick()
            }
        }
        val statusBarView = findViewById<View>(setStatusBarView())
        if (statusBarView != null) {
            ImmersionBar.setStatusBarView(this, statusBarView)
        }
        ImmersionBar.with(this)
                .statusBarColor(color)
                .navigationBarColor(R.color.white)
                .autoDarkModeEnable(true)
                .init()
    }

    fun setTitle(title: String?): TextView {
        val tv_title = titleBar!!.setTitle(title)
        return tv_title
    }

    fun setRightClick(text: String, listener: () -> Unit) {
        titleBar?.setRightClick(text, listener)
    }

    fun setRightClick(@DrawableRes res: Int, listener: () -> Unit) {
        titleBar?.setRightClick(res, listener)
    }

    fun setWhiteStyle() {
        titleBar?.setWhiteStyle()
        ImmersionBar.with(this)
                .statusBarColor(R.color.status_bar_color_transparent_black)
                .navigationBarColor(R.color.white)
                .statusBarDarkFont(true)
                .navigationBarDarkIcon(true)
                .init()
    }

    open fun setTitleBar() = R.id.title_bar

    open fun setStatusBarView() = R.id.view_status

    open protected fun leftClick() {
        finish()
    }

    abstract fun initView()

    override fun getResources(): Resources {
        if (Looper.getMainLooper().thread == Thread.currentThread()) {//解决某些手机某些情况下竖屏适配失败的问题
            AutoSizeCompat.autoConvertDensity(super.getResources(), 1080f, super.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        }
        val resources = super.getResources()
        var configuration = resources.getConfiguration()
        if (resources != null && configuration.fontScale != 1.0f) {
            configuration.fontScale = 1.0f
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics())
        return resources
    }

    override fun onConfigurationChanged(newConfig: Configuration) {//解决横屏无法适配的问题
        super.onConfigurationChanged(newConfig)
        AutoSize.autoConvertDensity(this, 1080f, super.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
    }

    override fun onResume() {
        super.onResume()
        Glide.with(this).resumeRequests()
    }

    override fun onClick(v: View?) {

    }

    /**
     * 点击软键盘外面的区域关闭软键盘
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        try {
            if (ev.action == MotionEvent.ACTION_DOWN) {
                // 获得当前得到焦点的View，
                val v = currentFocus
                if (isShouldHideInput(v, ev)) {
                    //根据判断关闭软键盘
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v?.windowToken, 0)
                }
            }
        } catch (e: Exception) {
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 判断用户点击的区域是否是输入框
     */
    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = (left
                    + v.getWidth())
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        return false
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        Glide.with(this).pauseRequests()
    }

    /**
     * 解决androidx在 Android 5.x版本上导致webView崩溃的问题
     */
    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 25) {
            overrideConfiguration!!.uiMode = overrideConfiguration.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv()
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }
}