package a.tlib.utils.retrofit

import a.tlib.R
import a.tlib.widget.loading.LoadingDia
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.orhanobut.logger.YLog


/**
 * @author 田桂森 2019/5/14
 */
/**
 * 耗时对话框工具类
 */
object ProgressDiaUtil {
    private val progressDia by lazy {
        LoadingDia.newInstance()
    }
    private var progressDia2: Dialog? = null

    @JvmStatic
    fun show(fragmentManager: FragmentManager?, tip: String = "加载中...") {
        try {
            progressDia.setTest(tip)
            progressDia.show(fragmentManager)
        } catch (e: Exception) {
            YLog.d(e.toString())
        }
    }

    @JvmStatic
    fun show(act: FragmentActivity?, tip: String = "加载中...") {
        try {
            progressDia.setTest(tip)
            progressDia.show(act)
        } catch (e: Exception) {
            YLog.d(e.toString())
        }
    }

    @JvmStatic
    fun show(fra: Fragment?, tip: String = "加载中...") {
        try {
            progressDia.setTest(tip)
            progressDia.show(fra)
        } catch (e: Exception) {
            YLog.d(e.toString())
        }
    }

    /**
     * 废弃
     */
    @Deprecated("废弃用上一个")
    @JvmStatic
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
    @JvmStatic
    fun dismiss() {
        try {
            if (progressDia.isVisible) {
                progressDia.dismiss()
            }
            if (progressDia2 != null && progressDia2?.isShowing()!!) {
                progressDia2?.dismiss()
            }
        } catch (e: Exception) {
            YLog.d(e.toString())
        }
    }
}