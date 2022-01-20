package a.tlib.widget.dialog.commonDialog

import a.tlib.R
import a.tlib.utils.putData
import a.tlib.widget.dialog.baseDialog.BaseTDialog
import android.view.View
import kotlinx.android.synthetic.main.dia_common_web.*

/**
 * @author 田桂森 2020/5/12 0012
 */
class CommonWebDia : BaseTDialog<CommonWebDia>() {
    companion object {
        val URL = "url"

        @JvmStatic
        fun newInstance(url: String): CommonWebDia = CommonWebDia().putData(URL to url)
    }

    init {
        setHeightScale(0.7f)
        setBottomStyle()
    }

    override val layoutId: Int = R.layout.dia_common_web

    override fun initView(view: View) {
        val url = arguments!!.getString(URL)!!
        webView.loadUrl(url)
    }
}