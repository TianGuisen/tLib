package a.tlib.utils

import androidx.core.content.ContextCompat
import a.tlib.LibApp


/**
 * @author 田桂森 2020/1/11 0011
 */

/**
 * 获得values里的colors
 */
fun getcolor(colorId: Int) = ContextCompat.getColor(LibApp.app, colorId)!!

/**
 * 获得values里的array
 */
fun getArrayInt(arrayId: Int) = LibApp.app.resources.getIntArray(arrayId)!!

/**
 * 获取drawable
 */
fun getDrawable(drawable: Int)= ContextCompat.getDrawable(LibApp.app, drawable)!!
