package a.tlib.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import a.tlib.R
import a.tlib.utils.getcolor
import a.tlib.utils.setSingClick
import com.ruffian.library.widget.RFrameLayout

class WebTitleBar : TitleBar {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

    override fun init() {
        view = LayoutInflater.from(context).inflate(R.layout.view_title_web, this)
        view.setBackgroundColor(getcolor(R.color.black))
        view.findViewById<RFrameLayout>(R.id.fl_close).setSingClick {

        }
    }

    fun setCloseClick(listener: (View) -> Unit) {
        view.findViewById<RFrameLayout>(R.id.fl_close).setOnClickListener {
            listener.invoke(it)
        }
    }
}
