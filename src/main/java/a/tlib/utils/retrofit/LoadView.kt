package a.tlib.utils.retrofit

import a.tlib.R
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.jeremyliao.liveeventbus.LiveEventBus
import java.util.*

/**
 * 一个方便在多种状态切换的view
 *  loadView的子布局必须要有id
 *  例如:ll
 *  要为哪个设置点击事件就把哪个id设置为:empty,error,tv_login,not_net
 */
class LoadView : FrameLayout {


    companion object {
        private val DEFAULT_LAYOUT_PARAMS = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        const val STATUS_CONTENT = 0//内容
        const val STATUS_LOADING = 1//加载
        const val STATUS_EMPTY = 2//空内容
        const val STATUS_ERROR = 3//错误
        const val STATUS_NO_NETWORK = 4///无网络
        const val STATUS_LOGIN = 5///登录

        private const val NULL_RESOURCE_ID = -1

        //默认的布局，需要在app中初始化
        @JvmField
        var defaultEmptyLayoutId = 0

        @JvmField
        var defaultErrorLayoutId = 0

        @JvmField
        var defaultLoadingLayoutId = 0

        @JvmField
        var defaultNoNetworkLayoutId = 0

        @JvmField
        var defaultLoginLayoutId = 0
    }

    var mEmptyView: View? = null
        private set
    var mErrorView: View? = null
        private set
    var mLoadingView: View? = null
        private set
    var mNoNetworkView: View? = null
        private set
    var mContentView: View? = null
        private set
    var mLoginView: View? = null
        private set

    var mEmptyViewResId: Int = 0
    var mErrorViewResId: Int = 0
    var mLoadingViewResId: Int = 0
    var mNoNetworkViewResId: Int = 0
    var mContentViewResId: Int = 0
    var mLoginViewResId: Int = 0

    var mEmptyIcon: Int = 0
    var mEmptyText: String = "还没有数据哦~"

    var enableErrorView = true//默认开启错误视图


    /**
     * 获取当前状态
     */
    var viewStatus: Int = 0
        private set
    private var mInflater: LayoutInflater? = null
    private var mOnRetryClickListener: View.OnClickListener? = null

    private val mOtherIds = ArrayList<Int>()

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val a = context!!.obtainStyledAttributes(attrs, R.styleable.LoadView, defStyleAttr, 0)
        mEmptyViewResId = a.getResourceId(R.styleable.LoadView_emptyView, defaultEmptyLayoutId)
        mErrorViewResId = a.getResourceId(R.styleable.LoadView_errorView, defaultErrorLayoutId)
        mLoadingViewResId = a.getResourceId(R.styleable.LoadView_loadingView, defaultLoadingLayoutId)
        mNoNetworkViewResId = a.getResourceId(R.styleable.LoadView_noNetworkView, defaultNoNetworkLayoutId)
        mContentViewResId = a.getResourceId(R.styleable.LoadView_contentView, NULL_RESOURCE_ID)
        mLoginViewResId = defaultLoginLayoutId
        a.recycle()
        mInflater = LayoutInflater.from(getContext())
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        showContent()
    }

    //在viewpager+fragment中销毁后存储状态
    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        val superData = super.onSaveInstanceState()
        bundle.putParcelable("super_data", superData)
        bundle.putInt("status", viewStatus)
        return bundle
    }

    //在viewpager+fragment中销毁后恢复状态
    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = state as Bundle
        val superData = bundle.getParcelable<Parcelable>("super_data")
        val status = bundle.getInt("status")
        showViewForStatus(status)
        super.onRestoreInstanceState(superData)
    }

//    override fun onDetachedFromWindow() {
//        super.onDetachedFromWindow()
//        clear(mEmptyView, mLoadingView, mErrorView, mNoNetworkView)
//        mOtherIds.clear()
//        if (null != mOnRetryClickListener) {
//            mOnRetryClickListener = null
//        }
//    }

    fun showViewForStatus(viewStatus: Int) {
        when (viewStatus) {
            STATUS_CONTENT -> {
                showContent()
            }
            STATUS_LOADING -> {
                showLoading()
            }
            STATUS_EMPTY -> {
                showEmpty()
            }
            STATUS_ERROR -> {
                showError()
            }
            STATUS_NO_NETWORK -> {
                showNoNetwork()
            }
        }
    }

    /**
     * 空列表时
     */
    fun setEmptyTextImg(text: String, imgId: Int = 0) {
        mEmptyText = text
        mEmptyIcon = imgId
    }

    /**
     * 空列表时显示的文字内容
     */
    fun setEmptyText(text: String) {
        mEmptyText = text
    }

    /**
     * 空列表时展示的图片
     */
    fun setEmptyIcon(id: Int) {
        mEmptyIcon = id
    }

    /**
     * 设置重试点击事件
     *
     * @param onRetryClickListener 重试点击事件
     */
    fun setOnRetryClickListener(onRetryListener: OnClickListener) {
        mOnRetryClickListener = onRetryListener
    }

    fun setOnRetryClickListener(onRetryListener: (Int) -> Unit) {
        mOnRetryClickListener = object : OnClickListener {
            override fun onClick(v: View?) {
                onRetryListener(viewStatus)
            }
        }
    }

    /**
     * 显示空视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    @JvmOverloads
    fun showEmpty(layoutId: Int = mEmptyViewResId, layoutParams: ViewGroup.LayoutParams = DEFAULT_LAYOUT_PARAMS) {

        showEmpty(if (null == mEmptyView) inflateView(layoutId) else mEmptyView, layoutParams)
    }

    /**
     * 显示空视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showEmpty(view: View?, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "Empty view is null.")
        checkNull(layoutParams, "Layout params is null.")
        viewStatus = STATUS_EMPTY
        if (null == mEmptyView) {
            mEmptyView = view
            val emptyRetryView = mEmptyView!!.findViewById<View>(R.id.empty)
            if (null != mOnRetryClickListener && null != emptyRetryView) {
                emptyRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mEmptyView!!.id)
            addView(mEmptyView, 0, layoutParams)
        }
        mEmptyView?.findViewById<TextView>(R.id.tv_empty)?.setText(mEmptyText)
        if (mEmptyIcon != 0) {
            mEmptyView?.findViewById<ImageView>(R.id.iv_icon)?.setImageDrawable(resources.getDrawable(mEmptyIcon))
        }
        showView(mEmptyView)
    }

    /**
     * 显示错误视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    @JvmOverloads
    fun showError(layoutId: Int = mErrorViewResId, layoutParams: ViewGroup.LayoutParams = DEFAULT_LAYOUT_PARAMS) {
        showError(if (null == mErrorView) inflateView(layoutId) else mErrorView, layoutParams)
    }

    /**
     * 显示登录视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    @JvmOverloads
    fun showLogin() {
        showLogin(if (null == mLoginView) inflateView(mLoginViewResId) else mLoginView, DEFAULT_LAYOUT_PARAMS)
    }

    /**
     * 显示登录视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showLogin(view: View?, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "Error view is null.")
        checkNull(layoutParams, "Layout params is null.")
        viewStatus = STATUS_LOGIN
        if (null == mLoginView) {
            mLoginView = view
            val errorRetryView = mLoginView!!.findViewById<View>(R.id.tv_login)
            if (null != errorRetryView) {
                errorRetryView.setOnClickListener {
                    LiveEventBus.get(ResCode.TOKEN_OVERDUE.toString()).post(null)
                }
            }
            mOtherIds.add(mLoginView!!.id)
            addView(mLoginView, 0, layoutParams)
        }
        showView(mLoginView)
    }

    /**
     * 显示错误视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showError(view: View?, layoutParams: ViewGroup.LayoutParams) {
        if (!enableErrorView) {
            showContent()
            return
        }
        checkNull(view, "Error view is null.")
        checkNull(layoutParams, "Layout params is null.")
        viewStatus = STATUS_ERROR
        if (null == mErrorView) {
            mErrorView = view
            val errorRetryView = mErrorView!!.findViewById<View>(R.id.error)
            if (null != mOnRetryClickListener && null != errorRetryView) {
                errorRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mErrorView!!.id)
            addView(mErrorView, 0, layoutParams)
        }
        showView(mErrorView)
    }

    /**
     * 显示加载中视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    @JvmOverloads
    fun showLoading(layoutId: Int = mLoadingViewResId, layoutParams: ViewGroup.LayoutParams = DEFAULT_LAYOUT_PARAMS) {
        showLoading(if (null == mLoadingView) inflateView(layoutId) else mLoadingView, layoutParams)
    }

    /**
     * 显示加载中视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showLoading(view: View?, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "Loading view is null.")
        checkNull(layoutParams, "Layout params is null.")
        viewStatus = STATUS_LOADING
        if (null == mLoadingView) {
            mLoadingView = view
            mOtherIds.add(mLoadingView!!.id)
            addView(mLoadingView, 0, layoutParams)
        }
        showView(mLoadingView)
    }

    /**
     * 显示无网络视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    @JvmOverloads
    fun showNoNetwork(layoutId: Int = mNoNetworkViewResId, layoutParams: ViewGroup.LayoutParams = DEFAULT_LAYOUT_PARAMS) {
        showNoNetwork(if (null == mNoNetworkView) inflateView(layoutId) else mNoNetworkView, layoutParams)
    }

    /**
     * 显示无网络视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showNoNetwork(view: View?, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "No network view is null.")
        checkNull(layoutParams, "Layout params is null.")
        viewStatus = STATUS_NO_NETWORK
        if (null == mNoNetworkView) {
            mNoNetworkView = view
            val noNetworkRetryView = mNoNetworkView!!.findViewById<View>(R.id.not_net)
            if (null != mOnRetryClickListener && null != noNetworkRetryView) {
                noNetworkRetryView.setOnClickListener(mOnRetryClickListener)
            }
            mOtherIds.add(mNoNetworkView!!.id)
            addView(mNoNetworkView, 0, layoutParams)
        }
        showView(mNoNetworkView)
    }

    /**
     * 显示内容视图
     */
    fun showContent() {
        viewStatus = STATUS_CONTENT
        if (null == mContentView && mContentViewResId != NULL_RESOURCE_ID) {
            mContentView = mInflater?.inflate(mContentViewResId, null)
            addView(mContentView, 0, DEFAULT_LAYOUT_PARAMS)
        }
        showContentView()
    }

    /**
     * 显示内容视图
     *
     * @param layoutId     自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showContent(layoutId: Int, layoutParams: ViewGroup.LayoutParams) {
        showContent(inflateView(layoutId), layoutParams)
    }

    /**
     * 显示内容视图
     *
     * @param view         自定义视图
     * @param layoutParams 布局参数
     */
    fun showContent(view: View, layoutParams: ViewGroup.LayoutParams) {
        checkNull(view, "Content view is null.")
        checkNull(layoutParams, "Layout params is null.")
        viewStatus = STATUS_CONTENT
        clear(mContentView)
        mContentView = view
        addView(mContentView, 0, layoutParams)
        showView(mContentView)
    }

    private fun inflateView(layoutId: Int): View {
        return mInflater!!.inflate(layoutId, null)
    }

    private fun showView(view: View?) {
        val childCount = childCount
        for (i in 0 until childCount) {
            val viewchild = getChildAt(i)
            if (viewchild == view) {
                viewchild?.visibility = View.VISIBLE
            } else {
                viewchild?.visibility = View.GONE
            }
        }
    }
//    private fun showView(viewId: Int) {
//        val childCount = childCount
//        for (i in 0 until childCount) {
//            val view = getChildAt(i)
//            if (view.id == viewId) {
//                view.visibility = View.VISIBLE
//            } else {
//                view.visibility = View.GONE
//            }
//        }
//    }

    private fun showContentView() {
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (mOtherIds.contains(view.id)) {
                view.visibility = View.GONE
            } else {
                view.visibility = View.VISIBLE
            }
        }
    }

    private fun checkNull(`object`: Any?, hint: String) {
        if (null == `object`) {
            throw NullPointerException(hint)
        }
    }

    private fun clear(vararg views: View?) {
        try {
            for (view in views) {
                if (null != view) {
                    removeView(view)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}