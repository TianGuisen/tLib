package a.tlib.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


/**
 * @author 田桂森 2020/3/20
 */
object KeyBoardUtil {
    /**
     * 显示软键盘
     *
     * @param view
     */
    @JvmStatic
    fun showSoftKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    @JvmStatic
    fun hideSoftKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * setSoftKeyBoard()
     * 设置软键盘显示/隐藏
     */
    @JvmStatic
    fun setSoftKeyBoard(context: Context) {
        //自动设置软键盘显示或隐藏
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    @JvmStatic
    fun showSoftKeyboard(view: View) {
        val imm = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm != null) {
            view.requestFocus()
            imm.showSoftInput(view, 0)
        }
    }

    @JvmStatic
    fun hideSoftKeyboard(view: View) {
        val imm = view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

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
    fun isShowKeyboard( v: View): Boolean {
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