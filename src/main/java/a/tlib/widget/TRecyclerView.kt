package a.tlib.widget

import a.tlib.R
import a.tlib.utils.AutoSizeUtil
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.cooltechworks.views.shimmer.ShimmerRecyclerView.LayoutMangerType


/**
 * @author 田桂森 2020/3/9
 */
open class TRecyclerView : ShimmerRecyclerView {
    /**
     * app:maxHeight="400dp"设置最大高度
     */
    private var mMaxHeight = -1

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        //默认隐藏加载的动画
        hideShimmerAdapter()
        overScrollMode = View.OVER_SCROLL_NEVER//隐藏边缘
        addOnScrollListener(imageLoadScrollListener)
        //android:orientation获取原生属性处理，不处理的话，会无效，有毒，默认是VERTICAL
        val a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerView)
        val orientation = a.getInt(androidx.recyclerview.R.styleable.RecyclerView_android_orientation, VERTICAL)
        if (layoutManager is LinearLayoutManager) {
            layoutManager = LinearLayoutManager(getContext(), orientation, false)
            if (orientation == VERTICAL) {
                setDemoLayoutManager(LayoutMangerType.LINEAR_VERTICAL)
            } else {
                setDemoLayoutManager(LayoutMangerType.LINEAR_HORIZONTAL)
            }
        }

        val arr = context.obtainStyledAttributes(attrs, R.styleable.TRecyclerView)
        if (arr != null) {//有手机出现null
            mMaxHeight = arr.getLayoutDimension(R.styleable.TRecyclerView_maxHeight, mMaxHeight)
        }
        arr?.recycle()
        if (itemAnimator is SimpleItemAnimator) {
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        if (mMaxHeight > 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(AutoSizeUtil.pt2px(mMaxHeight.toFloat()), MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        return super.onTouchEvent(e)
    }

    private val imageLoadScrollListener: OnScrollListener = object : OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
//            when (newState) {
//                SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING -> {//静止的时候加载图片
//                    try {
//                        if (context != null) {
//                            Glide.with(context).resumeRequests()
//                        }
//                    } catch (e: Exception) {
//                    }
//                }
//                SCROLL_STATE_SETTLING -> {//在滚动停止加载图片
//                    try {
//                        if (context != null) {
//                            Glide.with(context).pauseRequests()
//                        }
//                    } catch (e: Exception) {
//                    }
//                }
//            }
        }
    }

}