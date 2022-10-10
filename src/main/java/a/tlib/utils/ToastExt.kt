package a.tlib.utils

import a.tlib.LibApp
import a.tlib.R
import android.graphics.Color
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

/**
 * @author 田桂森 2021/6/7 0007
 */

@ColorInt
private val DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF")

@ColorInt
private val ERROR_COLOR = Color.parseColor("#FD4C5B")

@ColorInt
private val INFO_COLOR = Color.parseColor("#3F51B5")

@ColorInt
private val SUCCESS_COLOR = Color.parseColor("#389775")

@ColorInt
private val WARNING_COLOR = Color.parseColor("#FFA900")

/**
 * 显示默认的toast
 */
fun showNormalToast(
    message: Any?,
    duration: Int = Toast.LENGTH_SHORT,
    gravity: Int = Gravity.BOTTOM
) {
    ToastUtil.custom(
        LibApp.app, message.toString(), null,
        DEFAULT_TEXT_COLOR, duration, false
    )?.apply {
        setGravity(gravity, 0, 0)
        show()
    }
}
/**
 * 显示成功的toast
 */
fun showSuccessToast(
    message: Any?,
    duration: Int = Toast.LENGTH_SHORT,
    gravity: Int = Gravity.BOTTOM
) {
    ToastUtil.custom(
        LibApp.app, message.toString(), getDrawable(R.drawable.ic_check_white_48dp),
        DEFAULT_TEXT_COLOR, SUCCESS_COLOR, duration, true, true
    )?.apply {
        setGravity(gravity, 0, 0)
        show()
    }
}
/**
 * 显示错误的toast
 */
fun showErrorToast(
    message: Any?,
    duration: Int = Toast.LENGTH_SHORT,
    gravity: Int = Gravity.BOTTOM
) {
    ToastUtil.custom(
        LibApp.app, message.toString(), getDrawable(R.drawable.ic_clear_white_48dp),
        DEFAULT_TEXT_COLOR, ERROR_COLOR, duration, true, true
    )?.apply {
        setGravity(gravity, 0, 0)
        show()
    }
}
/**
 * 显示信息的toast
 */
fun showInfoToast(
    message: Any?,
    duration: Int = Toast.LENGTH_SHORT,
    gravity: Int = Gravity.BOTTOM
) {
    ToastUtil.custom(
        LibApp.app, message.toString(), getDrawable(R.drawable.ic_info_outline_white_48dp),
        DEFAULT_TEXT_COLOR, INFO_COLOR, duration, true, true
    )?.apply {
        setGravity(gravity, 0, 0)
        show()
    }
}
/**
 * 显示警告的toast
 */
fun showWarningToast(
    message: Any?,
    duration: Int = Toast.LENGTH_SHORT,
    gravity: Int = Gravity.BOTTOM
) {
    ToastUtil.custom(
        LibApp.app, message.toString(), getDrawable(R.drawable.ic_error_outline_white_48dp),
        DEFAULT_TEXT_COLOR, WARNING_COLOR, duration, true, true
    )?.apply {
        setGravity(gravity, 0, 0)
        show()
    }
}

/**
 * 显示自定义的toast
 */
fun showToast(
    message: Any?,
    @DrawableRes iconRes: Int, 
    @ColorInt textColor: Int,
    @ColorInt tintColor: Int, 
    withIcon: Boolean, 
    shouldTint: Boolean,
    duration: Int = Toast.LENGTH_SHORT,
    gravity: Int = Gravity.BOTTOM
) {
    ToastUtil.custom(
        LibApp.app, message.toString(), iconRes,
        textColor, tintColor, duration, withIcon, shouldTint
    )?.apply {
        setGravity(gravity, 0, 0)
        show()
    }
}