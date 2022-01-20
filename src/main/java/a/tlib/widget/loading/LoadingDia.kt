package a.tlib.widget.loading

import a.tlib.R
import a.tlib.widget.dialog.baseDialog.BaseTDialog
import android.view.View
import kotlinx.android.synthetic.main.dia_loading.*

/**
 *加载中弹窗
 */
class LoadingDia : BaseTDialog<LoadingDia>() {
    companion object {
        @JvmStatic
        fun newInstance() = LoadingDia()
    }

    init {
        setWidthDp(150f)
        setHeightDp(125f)
        setBackgroundLight()
        setCancelableOutside()
    }

    override val layoutId = R.layout.dia_loading
    private var showTest = "加载中..."
    override fun initView(view: View) {
        loading_view.setDelay(80)
        loading_view.setLoadingText(showTest)
    }

    fun setTest(text: String) {
        showTest = text
    }
}