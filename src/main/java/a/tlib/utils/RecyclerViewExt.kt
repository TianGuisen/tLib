@file:Suppress("unused")

package a.tlib.utils

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * RecyclerView extensions
 *
 * @author LiuBin
 * @version v1.0, 2019-05-23 15:48  由有播科技（杭州）有限公司开发
 */

/**
 * 精确移动到指定索引
 *
 * @param position 索引
 * @param offset 偏移量
 */
fun RecyclerView.scrollToPositionWithOffset(position: Int, offset: Int = 0) {
    when (val lm = layoutManager) {
        is StaggeredGridLayoutManager -> lm.scrollToPositionWithOffset(position, offset)
        is LinearLayoutManager -> lm.scrollToPositionWithOffset(position, offset)
    }
}
/** 移动到顶部 */
fun RecyclerView.scrollToTop() {
    scrollToPositionWithOffset(0)
}
/** 移动到底部 */
fun RecyclerView.scrollToBottom() {
    adapter?.let {
        scrollToPositionWithOffset(it.itemCount - 1)
    }
}
/**
 * RecyclerView快速配置
 *
 * @param layoutManager LayoutManager，默认垂直方向布局
 * @param hasFixedSize 布局高度是否固定，默认固定
 * @param supportsChangeAnimations 是否展示动画，默认不展示
 * @param decoration 分割线，默认无
 * @param adapter 适配器
 */
fun RecyclerView.applyLinearConfig(
        layoutManager: RecyclerView.LayoutManager = WrapLinearLayoutManager(context),
        hasFixedSize: Boolean = true,
        supportsChangeAnimations: Boolean = false,
        decoration: ItemDecoration? = null,
        adapter: RecyclerView.Adapter<*>? = null
) {
    setHasFixedSize(hasFixedSize)
    this.layoutManager = layoutManager
    if (!supportsChangeAnimations) (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    if (null != decoration) addItemDecoration(decoration)
    if (null != adapter) this.adapter = adapter
}
/**
 * RecyclerView快速配置（水平方向）
 *
 * @param hasFixedSize 布局高度是否固定，默认固定
 * @param supportsChangeAnimations 是否展示动画，默认不展示
 * @param decoration 分割线，默认无
 * @param adapter 适配器，默认无
 */
fun RecyclerView.applyHorizontalLinearConfig(
    hasFixedSize: Boolean = true,
    supportsChangeAnimations: Boolean = false,
    decoration: ItemDecoration? = null,
    adapter: RecyclerView.Adapter<*>? = null
) {
    val lm = WrapLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    applyLinearConfig(lm, hasFixedSize, supportsChangeAnimations, decoration, adapter)
}
/**
 * RecyclerView快速配置（网格布局）
 *
 * @param spanCount 列数
 * @param hasFixedSize 布局高度是否固定，默认固定
 * @param supportsChangeAnimations 是否展示动画，默认不展示
 * @param decoration 分割线，默认无
 * @param adapter 适配器，默认无
 */
fun RecyclerView.applyGridConfig(
    spanCount: Int,
    hasFixedSize: Boolean = true,
    supportsChangeAnimations: Boolean = false,
    decoration: ItemDecoration? = null,
    adapter: RecyclerView.Adapter<*>? = null
) {
    setHasFixedSize(hasFixedSize)
    this.layoutManager = GridLayoutManager(context, spanCount)
    if (!supportsChangeAnimations) (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    if (null != decoration) addItemDecoration(decoration)
    if (null != adapter) this.adapter = adapter
}
/**
 * RecyclerView快速配置（瀑布流）
 *
 * @param spanCount 列数
 * @param supportsChangeAnimations 是否展示动画，默认不展示
 * @param decoration 分割线，默认无
 * @param adapter 适配器，默认无
 */
fun RecyclerView.applyStaggeredGridConfig(
    spanCount: Int,
    supportsChangeAnimations: Boolean = false,
    decoration: ItemDecoration? = null,
    adapter: RecyclerView.Adapter<*>? = null
) {
    setHasFixedSize(false)
    this.layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    if (!supportsChangeAnimations) (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    if (null != decoration) addItemDecoration(decoration)
    if (null != adapter) this.adapter = adapter
}

/**
 * Fix:
 *
 * > java.lang.IndexOutOfBoundsException: Inconsistency detected.
 * > Invalid view holder adapter positionViewHolder
 *
 * - [RecyclerView IndexOutOfBoundsException: Inconsistency detected](https://stackoverflow.com/questions/31759171/recyclerview-and-java-lang-indexoutofboundsexception-inconsistency-detected-in)
 */
class WrapLinearLayoutManager : LinearLayoutManager {
    constructor(context: Context) : super(context)

    constructor(context: Context, orientation: Int, reverseLayout: Boolean):
        super(context, orientation, reverseLayout)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int):
        super(context, attrs, defStyleAttr, defStyleRes)

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }
}