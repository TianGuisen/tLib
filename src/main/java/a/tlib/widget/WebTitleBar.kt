package a.tlib.widget

import a.tlib.R
import a.tlib.utils.getcolor
import a.tlib.utils.hide
import a.tlib.utils.setSingClick
import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.ruffian.library.widget.RFrameLayout
import com.ruffian.library.widget.RView

class WebTitleBar : TitleBar {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    override fun init() {
        view = RView.inflate(context, R.layout.view_title_web, this) as TitleBar
        view.helper.setBackgroundColorNormal(getcolor(R.color.black))
        tv_title = view.findViewById(R.id.tv_title)
        fl_back = view.findViewById(R.id.fl_back)
        iv_back = view.findViewById(R.id.iv_back)
        iv_right = view.findViewById(R.id.iv_right)
        fl_right = view.findViewById(R.id.fl_right)
        view_line = view.findViewById(R.id.view_line)
        view.findViewById<RFrameLayout>(R.id.fl_close).setSingClick {

        }
    }

    fun hideClose() {
        view.findViewById<RFrameLayout>(R.id.fl_close).hide()
    }

    fun setCloseClick(listener: (View) -> Unit) {
        view.findViewById<RFrameLayout>(R.id.fl_close).setOnClickListener {
            listener.invoke(it)
        }
    }
}
