package a.tlib.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


/**
 * @author 田桂森 2020/3/20
 */
object KeyBoardUtil {

    /**
     * 显示键盘
     */
    @JvmStatic
    fun showSoftKeyboard(act: Activity) {
        val v = act.currentFocus ?: return
        (act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(v, 0)
    }

    /**
     * 显示键盘
     */
    @JvmStatic
    fun showSoftKeyboard(view: View) {
        val imm = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm != null) {
            view.requestFocus()
            imm.showSoftInput(view, 0)
        }
    }

    /**
     * 隐藏键盘
     */
    @JvmStatic
    fun hideSoftKeyboard(act: Activity) {
        try {
            val v = act.window.currentFocus
            if (v != null) {
                val imm = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
        } catch (e: Exception) {
        }
    }

    /**
     * 隐藏键盘
     */
    @JvmStatic
    fun hideSoftKeyboard(view: View) {
        val imm = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 切换软键盘的状态
     * 如当前为收起变为弹出,若当前为弹出变为收起
     */
    @JvmStatic
    fun setSoftKeyBoard(view: View) {
        val imm = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.toggleSoftInput(0, 0)
    }

    /**
     * 判断软键盘是否弹出
     */
    @JvmStatic
    fun isShowKeyboard(v: View): Boolean {
        val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return if (imm.hideSoftInputFromWindow(v.windowToken, 0)) {
            imm.showSoftInput(v, 0)
            true
            //软键盘已弹出
        } else {
            false
            //软键盘未弹出
        }
    }
}