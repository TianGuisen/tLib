package a.tlib.utils.retrofit

import a.tlib.R
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.orhanobut.logger.YLog


/**
 * @author 田桂森 2019/5/14
 */
/**
 * 耗时对话框工具类
 */
object ProgressDiaUtil {
    private var progressDia: Dialog? = null
    private var progressDia2: Dialog? = null
    fun show(context: Context?, tip: String? = "加载中...") {
        try {
            if (progressDia != null && progressDia!!.isShowing) {
                progressDia!!.dismiss()
            }
            progressDia = Dialog(context!!, R.style.CustomProgressDialog_info)
            val loadView = LayoutInflater.from(context).inflate(R.layout.dia_custom_progress, null)
            progressDia?.setContentView(loadView)
            progressDia?.setCanceledOnTouchOutside(false)
            val tvTip = loadView.findViewById<TextView>(R.id.tvTip)
            tvTip.setText(tip)
            progressDia?.show()
        } catch (e: Exception) {
            YLog.d(e.toString())
        }
    }

    fun show(context: Context?) {
        try {
            if (progressDia2 != null && progressDia2!!.isShowing) {
                progressDia2!!.dismiss()
            }
            progressDia2 = Dialog(context!!, R.style.CustomProgressDialog_info)
            val loadView = LayoutInflater.from(context).inflate(R.layout.dia_custom_progress, null)
            progressDia2?.setContentView(loadView)
            progressDia2?.setCanceledOnTouchOutside(false)
            val tvTip = loadView.findViewById<TextView>(R.id.tvTip)
            tvTip.setText("加载中...")
            progressDia2?.show()
        } catch (e: Exception) {
            YLog.d(e.toString())
        }
    }

    /**
     * 隐藏耗时对话框
     */
    fun dismiss() {
        try {
            if (progressDia != null && progressDia?.isShowing()!!) {
                progressDia?.dismiss()
            }
            if (progressDia2 != null && progressDia2?.isShowing()!!) {
                progressDia2?.dismiss()
            }
        } catch (e: Exception) {
            YLog.d(e.toString())
        }
    }
}