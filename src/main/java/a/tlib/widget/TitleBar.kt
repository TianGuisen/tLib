package a.tlib.widget

import a.tlib.R
import a.tlib.base.BaseTitleBar
import android.content.Context
import android.util.AttributeSet

/**
 * @author 田桂森 2022/2/8 0008
 */
class TitleBar : BaseTitleBar {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun layoutId(): Int {
        return R.layout.view_title
    }

}