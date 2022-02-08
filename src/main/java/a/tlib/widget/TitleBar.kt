package a.tlib.widget

import a.tlib.R
import a.tlib.base.BaseTitleBar
import android.content.Context
import android.util.AttributeSet

/**
 * @author 田桂森 2022/2/8 0008
 */
class TitleBar : BaseTitleBar {
    override var layoutId: Int= R.layout.view_title
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

}