package a.tlib.base

import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * @author 田桂森 2021/4/28 0028
 */
abstract class BaseTAdapter<T : IRVListBean>(layoutId: Int) : BaseQuickAdapter<T, MyViewHolder>(layoutId) {
    init {
        setAnimationWithDefault(AnimationType.AlphaIn)
    }
}