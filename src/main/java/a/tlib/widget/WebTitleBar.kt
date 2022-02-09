package a.tlib.widget

import a.tlib.R
import a.tlib.base.BaseTitleBar
import a.tlib.utils.getcolor
import a.tlib.utils.hide
import a.tlib.utils.setSingClick
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.ruffian.library.widget.RFrameLayout
import com.ruffian.library.widget.RView

class WebTitleBar : BaseTitleBar {


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun layoutId(): Int {
        return R.layout.view_title
    }

    fun hideClose() {
        findViewById<RFrameLayout>(R.id.fl_close).hide()
    }

    fun setCloseClick(listener: (View) -> Unit) {
        findViewById<RFrameLayout>(R.id.fl_close).setOnClickListener {
            listener.invoke(it)
        }
    }
}
