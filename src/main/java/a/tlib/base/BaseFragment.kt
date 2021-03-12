package a.tlib.base

import a.tlib.R
import a.tlib.utils.gson.GsonUtil
import a.tlib.widget.TitleBar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.components.ImmersionFragment
import a.tlib.utils.StringUtils
import android.content.res.Configuration
import me.jessyan.autosize.internal.CustomAdapt


abstract class BaseFragment : ImmersionFragment(), View.OnClickListener, CustomAdapt {
    val TAG = this.javaClass.simpleName
    lateinit var mRootView: View
    lateinit var act: AppCompatActivity
    var gson = GsonUtil.gson//后期去除，使用GsonUtil
    var strUtils = StringUtils()//后期要去除
    abstract var layoutId: Int   // 布局文件
    var titleBar: TitleBar? = null//标题栏
    var statusBarView: View? = null//状态填充兰
    var isFirstLoadData = true//第一次创建的时候
    var isFirstShowLoadData = true//第一次显示的时候
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(layoutId, null)
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("onCreateFra", this.javaClass.simpleName)//用于快速定位页面对应的代码
        act = activity!! as AppCompatActivity
        titleBar = view.findViewById(setTitleBar())
        statusBarView = view.findViewById<View>(setStatusBarView())

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
    override fun initImmersionBar() {
        titleBar?.let {
            ImmersionBar.setTitleBar(this, titleBar)
            it.setOnLeftImageListener {
                activity?.finish()
            }
        }
        if (statusBarView != null) {
            ImmersionBar.setStatusBarView(this, statusBarView)
        }
        ImmersionBar.with(this)
                .statusBarColor(R.color.status_bar_color_transparent_black)
                .navigationBarColor(R.color.white)
                .statusBarDarkFont(true)
//                .navigationBarDarkIcon(true)
//                .autoDarkModeEnable(true)
                .init()
    }

    fun setTitle(title: String): TextView {
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

    override fun onVisible() {
        super.onVisible()
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

    override fun onClick(v: View?) {

    }

    override fun onResume() {
        super.onResume()
        Glide.with(this).resumeRequests()
    }

    override fun onPause() {
        super.onPause()
        Glide.with(this).pauseRequests()
    }

    override fun isBaseOnWidth(): Boolean {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
    }

    override fun getSizeInDp(): Float {
        return 1080f
    }
}
