package a.tlib.base

import a.tlib.R
import a.tlib.widget.TitleBar
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.components.ImmersionProxy
import me.jessyan.autosize.internal.CustomAdapt


abstract class BaseTFragment(@androidx.annotation.LayoutRes layoutId:Int) : Fragment(layoutId), CustomAdapt {
    val TAG = this.javaClass.simpleName
    lateinit var rootView: View
    lateinit var act: AppCompatActivity
    var titleBar: TitleBar? = null//标题栏
    var statusBarView: View? = null//状态填充兰
    var isFirstLoadData = true//第一次创建的时候
    var isFirstShowLoadData = true//第一次显示的时候
    private val mImmersionProxy = ImmersionProxy(this)
    /**
     * 监控了当前act的协程作用域
     */
    val LifecycleOwner.lifecycleScope: LifecycleCoroutineScope
        get() = lifecycle.coroutineScope
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mImmersionProxy.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mImmersionProxy.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("onCreateFra", this.javaClass.simpleName)//用于快速定位页面对应的代码
        rootView=view
        act = requireActivity() as AppCompatActivity
        titleBar = view.findViewById(setTitleBar())
        statusBarView = view.findViewById<View>(setStatusBarView())
        titleBar?.setOnLeftImageListener {
            activity?.finish()
        }
        titleBar?.hideBack()
        initView()
        if (isFirstLoadData) {
            isFirstLoadData = false
            firstLoadData()
        } else {
            notFirstLoadData()
        }
    }

    /**
     * 初始化沉浸标题栏
     */
    fun initImmersionBar() {
        ImmersionBar.setTitleBar(this, titleBar)
        if (statusBarView != null) {
            ImmersionBar.setStatusBarView(this, statusBarView)
        }
//        ImmersionBar.with(this)
//            .statusBarColor(R.color.status_bar_color_transparent_black)
//            .navigationBarColor(R.color.white)
//            .statusBarDarkFont(true)
////                .navigationBarDarkIcon(true)
////                .autoDarkModeEnable(true)
//            .init()
    }

    fun setTitle(title: String?): TextView {
        val tv_title = titleBar!!.setTitle(title)
        return tv_title
    }

    /**
     * titleBar默认id=title_bar
     */
    open fun setTitleBar() = R.id.title_bar

    /**
     * 状态栏默认id=view_status
     */
    open fun setStatusBarView() = R.id.view_status

    /**
     * 初始化控件
     */
    protected abstract fun initView()

    /**
     * childFragmentManager或者fragmentManager
     * framgent切换
     */
    fun showHideFragment(fm: FragmentManager, showTFragment: BaseTFragment, hideTFragment: BaseTFragment? = null, flId: Int = R.id.fl) {
        fm.beginTransaction().apply {
            if (showTFragment === hideTFragment) return
            if (showTFragment.isFirstLoadData && !showTFragment.isAdded) {
                add(flId, showTFragment, showTFragment.javaClass.simpleName)
            }
            if (hideTFragment != null && !hideTFragment.isHidden) {
                hide(hideTFragment)
            }
            show(showTFragment)
            commitAllowingStateLoss()
        }
    }

    fun onVisible() {
        if (isFirstShowLoadData) {
            isFirstShowLoadData = false
            firstShowLoadData()
        }
    }

    /**
     * 在fragment创建的时候调用，不会重复调用
     * fragment的view销毁后，framgent对象以及成员属性并不会销毁
     */
    open protected fun firstLoadData() {

    }

    open protected fun notFirstLoadData() {

    }

    /**
     * 在fragment第一次显示的时候调用，不会重复调用
     */
    open protected fun firstShowLoadData() {

    }

    override fun onResume() {
        super.onResume()
        mImmersionProxy.onResume()
        Glide.with(this).resumeRequests()
    }

    override fun onPause() {
        super.onPause()
        mImmersionProxy.onPause()
        Glide.with(this).pauseRequests()
    }

    override fun isBaseOnWidth(): Boolean {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
    }

    override fun getSizeInDp(): Float {
        return 1080f
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mImmersionProxy.isUserVisibleHint = isVisibleToUser
    }


    override fun onDestroy() {
        super.onDestroy()
        mImmersionProxy.onDestroy()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mImmersionProxy.onHiddenChanged(hidden)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mImmersionProxy.onConfigurationChanged(newConfig)
    }

    /**
     * 懒加载，在view初始化完成之前执行
     * On lazy after view.
     */
    open fun onLazyBeforeView() {}

    /**
     * 懒加载，在view初始化完成之后执行
     * On lazy before view.
     */
    open fun onLazyAfterView() {}

    /**
     * Fragment用户不可见时候调用
     * On invisible.
     */
    open fun onInvisible() {}

    /**
     * 是否可以实现沉浸式，当为true的时候才可以执行initImmersionBar方法
     * Immersion bar enabled boolean.
     *
     * @return the boolean
     */
    open fun immersionBarEnabled(): Boolean {
        return true
    }

//    fun getStringExtra(key: String): String {
//        return arguments?.getString(key) ?: ""
//    }
//
//    fun getIntExtra(key: String, defaultValue: Int = 0): Int {
//        return arguments?.getInt(key, defaultValue) ?: defaultValue
//    }
//
//    fun getDoubleExtra(key: String, defaultValue: Double = 0.0): Double {
//        return arguments?.getDouble(key, defaultValue) ?: defaultValue
//    }
//
//    fun getLongExtra(key: String, defaultValue: Long = 0): Long {
//        return arguments?.getLong(key, defaultValue) ?: defaultValue
//    }
//
//
//    fun getBooleanExtra(key: String, defaultValue: Boolean = false): Boolean {
//        return arguments?.getBoolean(key, defaultValue) ?: defaultValue
//    }
//
//    fun <T> getSerializableExtra(key: String): T? {
//        arguments?.getSerializable(key)?.let {
//            return it as T
//        }
//        return null
//    }
//
//    fun <T> getListExtra(key: String): MutableList<T> {
//        arguments?.getSerializable(key)?.let {
//            return it as MutableList<T>
//        }
//        return mutableListOf()
//    }
}
