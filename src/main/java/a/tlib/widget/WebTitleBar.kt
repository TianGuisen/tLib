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

    override var layoutId: Int = R.layout.view_title_web

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

//
//    override fun init() {
//        view = RView.inflate(context, R.layout.view_title_web, this) as TitleBar
//        view.helper.setBackgroundColorNormal(getcolor(R.color.black))
//        view.findViewById<RFrameLayout>(R.id.fl_close).setSingClick {
//
//        }
//    }
//
    fun hideClose() {
        findViewById<RFrameLayout>(R.id.fl_close).hide()
    }

    fun setCloseClick(listener: (View) -> Unit) {
        findViewById<RFrameLayout>(R.id.fl_close).setOnClickListener {
            listener.invoke(it)
        }
    }
}
