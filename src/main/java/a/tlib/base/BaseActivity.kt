package a.tlib.base

import a.tlib.R
import a.tlib.utils.AppUtil
import a.tlib.utils.StringUtils
import a.tlib.utils.getcolor
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
import a.tlib.logger.YLog
import android.app.Activity
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeCompat

abstract class BaseActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        /**
         * startActForResult时候默认code
         */
        const val REQUEST_CODE = 10

        /**
         * startActForResult时候putExtra默认的key
         */
        const val EXTRA_KEY = "extra_key"
    }

    val TAG = this.javaClass.simpleName

    @Deprecated("废弃")
    var strUtils = StringUtils()
    abstract val layoutId: Int
    var titleBar: TitleBar? = null
    lateinit var act: BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = this
//        ARouter.getInstance().inject(this)  // Start auto inject.
        WebView.enableSlowWholeDocumentDraw()
        if (layoutId > 0) {
            setContentView(layoutId)
        }
        titleBar = findViewById(setTitleBar())
        titleBar?.setOnLeftImageListener {
            leftClick()
        }
        initView()
    }

    fun setTitle(title: String?, statusBarColor: Int = R.color.transparent, navigationBarColor: Int = -1): TextView? {
        ImmersionBar.setTitleBar(this, titleBar)
        val statusBarView = findViewById<View>(setStatusBarView())
        if (statusBarView != null) {
            ImmersionBar.setStatusBarView(this, statusBarView)
        }
        val tv_title = titleBar?.setTitle(title)
        val navigationBarColor = if (navigationBarColor > 0) navigationBarColor else R.color.black
        ImmersionBar.with(this)
                .statusBarColor(statusBarColor)//状态栏颜色
                .navigationBarColor(navigationBarColor)//导航栏颜色
                .statusBarDarkFont(false)//状态栏图标浅色
                .autoNavigationBarDarkModeEnable(true, 0.2f)
//                        .navigationBarDarkIcon(!AppUtil.isDarkColor(navigationBarColor))//导航栏图标浅色
                .init()
        return tv_title
    }

    fun setRightClick(text: String, listener: () -> Unit) {
        titleBar?.setRightClick(text, listener)
    }

    fun setRightClick(@DrawableRes res: Int, listener: () -> Unit) {
        titleBar?.setRightClick(res, listener)
    }

    open fun setTitleBar() = R.id.title_bar

    open fun setStatusBarView() = R.id.view_status

    open protected fun leftClick() {
        finish()
    }

    abstract fun initView()

    /**
     * framgent切换
     */
    fun showHideFragment(showFragment: BaseFragment, hideFragment: BaseFragment? = null, flId: Int = R.id.fl) {
        supportFragmentManager.beginTransaction().apply {
            if (showFragment === hideFragment) return
            if (showFragment.isFirstLoadData && !showFragment.isAdded) {
                add(flId, showFragment, showFragment.javaClass.simpleName)
            }
            if (hideFragment != null && !hideFragment.isHidden) {
                hide(hideFragment)
            }
            show(showFragment)
            commitAllowingStateLoss()
        }
    }

    override fun getResources(): Resources {
        if (Looper.getMainLooper().thread == Thread.currentThread()) {//解决某些手机某些情况下竖屏适配失败的问题
            AutoSizeCompat.autoConvertDensity(
                    super.getResources(),
                    1080f,
                    super.getResources()
                            .getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
            )
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
        AutoSize.autoConvertDensity(
                this,
                1080f,
                super.getResources()
                        .getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
        )
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
            val right = (left + v.getWidth())
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

    fun getStringExtra(key: String, default: String = ""): String {
        intent.getStringExtra(key).let {
            return it ?: default
        }
    }

    fun getIntExtra(key: String, default: Int = 0): Int {
        return intent.getIntExtra(key, default)
    }

    fun getDoubleExtra(key: String, defaultValue: Double = 0.0): Double {
        return intent.getDoubleExtra(key, defaultValue)
    }

    fun getLongExtra(key: String, defaultValue: Long = 0): Long {
        return intent.getLongExtra(key, defaultValue)
    }

    fun getBooleanExtra(key: String, default: Boolean = false): Boolean {
        return intent.getBooleanExtra(key, default)
    }

    fun <T> getSerializableExtra(key: String): T? {
        intent.getSerializableExtra(key)?.let {
            return it as T
        }
        return null
    }

    fun <T> getListExtra(key: String): MutableList<T>? {
        intent.getSerializableExtra(key)?.let {
            return it as MutableList<T>
        }
        return mutableListOf()
    }


}