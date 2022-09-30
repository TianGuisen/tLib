package a.tlib.utils

import androidx.core.content.ContextCompat
import a.tlib.LibApp


/**
 * @author 田桂森 2020/1/11 0011
 */

/**
 * 获得values里的colors
 */
fun getcolor(colorId: Int) = ContextCompat.getColor(LibApp.app, colorId)

/**
 * 获得values里的array
 */
fun getArrayInt(arrayId: Int) = LibApp.app.resources.getIntArray(arrayId)

/**
 * 获取drawable
 */
fun getDrawable(drawable: Int)= ContextCompat.getDrawable(LibApp.app, drawable)

/**
 * 获得values里的colors
 */
fun Int.getColorRes() = ContextCompat.getColor(LibApp.app, this)

/**
 * 获得values里的string
 */
fun Int.getStringRes() = LibApp.app.resources.getString(this)

/**
 * 获得values里的array
 */
fun Int.getArrayRes() = LibApp.app.resources.getIntArray(this)

/**
 * 获取drawable
 */
fun Int.getDrawableRes() = ContextCompat.getDrawable(LibApp.app, this)!!

