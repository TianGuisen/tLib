package a.tlib.utils

import a.tlib.LibApp
import android.util.TypedValue

/**
 * @author 田桂森 2019/12/26 0026
 */
object AutoSizeUtil {
    @JvmStatic
    fun dp2px(value: Float): Int {
        return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, LibApp.app.resources.displayMetrics) + 0.5f).toInt()
    }
    @JvmStatic
    fun sp2px(value: Float): Int {
        return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, LibApp.app.resources.displayMetrics) + 0.5f).toInt()
    }
    @JvmStatic
    fun pt2px(value: Float): Int {
        return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, value, LibApp.app.resources.displayMetrics) + 0.5f).toInt()
    }
    @JvmStatic
    fun in2px(value: Float): Int {
        return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, value, LibApp.app.resources.displayMetrics) + 0.5f).toInt()
    }
    @JvmStatic
    fun mm2px(value: Float): Int {
        return (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, value, LibApp.app.resources.displayMetrics) + 0.5f).toInt()
    }
    @JvmStatic
    fun px2dp(px: Double): Double {
        val density = LibApp.app.getResources().getDisplayMetrics().density
        return (px / density + 0.5)
    }
}