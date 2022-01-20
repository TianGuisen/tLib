package a.tlib.widget.recyclerDecoration

import a.tlib.utils.AutoSizeUtil
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * @author 田桂森 2019/6/14
 * 废弃，使用SpacesItemDecoration
 * 空格分隔线
 */
@Deprecated("废弃")
class TSpaceItemDecoration : RecyclerView.ItemDecoration() {
    private var firstMarginTop: Int? = null
    private var firstMarginLeft: Int? = null
    private var lastMarginBottm: Int? = null
    private var lastMarginRight: Int? = null
    private var horizontalSpace: Int? = null
    private var verticalSpace: Int? = null

    fun setFirstMarginTop(top: Int): TSpaceItemDecoration {
        firstMarginTop = AutoSizeUtil.pt2px(top.toFloat())
        return this
    }

    fun setFirstMarginLeft(left: Int): TSpaceItemDecoration {
        firstMarginLeft = AutoSizeUtil.pt2px(left.toFloat())
        return this
    }

    fun setLastMarginBottm(bottm: Int): TSpaceItemDecoration {
        lastMarginBottm = AutoSizeUtil.pt2px(bottm.toFloat())
        return this
    }

    fun setLastMarginRight(right: Int): TSpaceItemDecoration {
        lastMarginRight = AutoSizeUtil.pt2px(right.toFloat())
        return this
    }

    /**
     * 设置2个item之间的间距,单位pt
     */
    fun setHorizontalSpace(space: Int): TSpaceItemDecoration {
        horizontalSpace = AutoSizeUtil.pt2px(space.toFloat()) / 2
        return this
    }

    /**
     * 设置2个item之间的间距,单位pt
     */
    fun setVerticalSpace(space: Int): TSpaceItemDecoration {
        verticalSpace = AutoSizeUtil.pt2px(space.toFloat()) / 2
        return this
    }
    /**
     * 设置2个item之间的间距,DP
     */
    fun setVerticalSpaceDp(space: Int): TSpaceItemDecoration {
        verticalSpace = AutoSizeUtil.dp2px(space.toFloat()) / 2
        return this
    }
    //设置ItemView的内嵌偏移长度（inset）
    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        //获取view所在的绝对位置
        val position = parent.getChildAdapterPosition(view)
        if (horizontalSpace != null) {
            outRect.left = horizontalSpace as Int
            outRect.right = horizontalSpace as Int
            if (position == 0) {
                outRect.left = horizontalSpace!!*2
            }
            if (position == parent.adapter?.itemCount!! - 1) {
                outRect.right = horizontalSpace!!*2
            }
        }
        if (verticalSpace != null) {
            outRect.top = verticalSpace as Int
            outRect.bottom = verticalSpace as Int
            if (position == 0) {
                outRect.top = verticalSpace!! *2
            }
            if (position == parent.adapter?.itemCount!! - 1) {
                outRect.bottom = verticalSpace!!*2
            }
        }
        if (firstMarginTop != null) {
            if (position == 0) {
                outRect.top = firstMarginTop!!
            }
        }
        if (firstMarginLeft != null) {
            if (position == 0) {
                outRect.left = firstMarginLeft!!
            }
        }
        if (lastMarginBottm != null) {
            if (position == parent.adapter?.itemCount!! - 1) {
                outRect.bottom = lastMarginBottm!!
            }
        }
        if (lastMarginRight != null) {
            if (position == parent.adapter?.itemCount!! - 1) {
                outRect.right = lastMarginRight!!
            }
        }
    }

    // 在子视图上设置绘制范围，并绘制内容
    // 绘制图层在ItemView以下，所以如果绘制区域与ItemView区域相重叠，会被遮挡
    override fun onDraw(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    //同样是绘制内容，但与onDraw（）的区别是：绘制在图层的最上层
    override fun onDrawOver(c: Canvas, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        // 获取RecyclerView当前屏幕Child view的个数
//        val childCount = parent.childCount
        // 获取所有Child view的个数
//        val itemCount = parent.adapter?.itemCount
        // 通过Canvas绘制矩形（分割线）
//            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
    }
}