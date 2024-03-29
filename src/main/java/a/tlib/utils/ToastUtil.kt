package a.tlib.utils

import a.tlib.R
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.NinePatchDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
@Deprecated("使用ToastExt")
object ToastUtil {
    private lateinit var appContext: Context
    fun init(context: Context) {
        appContext = context
    }

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

    private val TOAST_TYPEFACE = "sans-serif-condensed"

    private var currentToast: Toast? = null

    @Deprecated("使用ToastExt")
    @JvmStatic
    fun normal(message: String?) {
        normal(appContext, message, Toast.LENGTH_SHORT, null, false)?.show()
    }

    @Deprecated("使用ToastExt")
    fun normal(message: String?, icon: Drawable) {
        normal(appContext, message, Toast.LENGTH_SHORT, icon, true)?.show()
    }

    @Deprecated("使用ToastExt")
    fun normal(message: String?, duration: Int) {
        normal(appContext, message, duration, null, false)?.show()
    }

    fun normal(message: String?, duration: Int, icon: Drawable) {
        normal(appContext, message, duration, icon, true)?.show()
    }

    fun normal(message: String?, duration: Int, icon: Drawable, withIcon: Boolean): Toast? {
        return custom(appContext, message, icon, DEFAULT_TEXT_COLOR, duration, withIcon)
    }

    fun warning(message: String?) {
        warning(appContext, message, Toast.LENGTH_SHORT, true)?.show()
    }

    fun warning(message: String?, duration: Int) {
        warning(appContext, message, duration, true)?.show()
    }

    fun warning(message: String?, duration: Int, withIcon: Boolean): Toast? {
        return custom(
            appContext,
            message,
            getDrawable(appContext, R.drawable.ic_error_outline_white_48dp),
            DEFAULT_TEXT_COLOR,
            WARNING_COLOR,
            duration,
            withIcon,
            true
        )
    }

    @Deprecated("使用ToastExt")
    fun info(message: String?) {
        info(appContext, message, Toast.LENGTH_SHORT, true)?.show()
    }

    fun info(message: String?, duration: Int) {
        info(appContext, message, duration, true)?.show()
    }

    fun info(message: String?, duration: Int, withIcon: Boolean): Toast? {
        return custom(
            appContext,
            message,
            getDrawable(appContext, R.drawable.ic_info_outline_white_48dp),
            DEFAULT_TEXT_COLOR,
            INFO_COLOR,
            duration,
            withIcon,
            true
        )
    }

    @Deprecated("使用ToastExt")
    fun success(message: String?) {
        success(appContext, message, Toast.LENGTH_SHORT, true)?.show()
    }

    fun success(message: String?, duration: Int) {
        success(appContext, message, duration, true)?.show()
    }

    fun success(message: String?, duration: Int, withIcon: Boolean): Toast? {
        return custom(
            appContext,
            message,
            getDrawable(appContext, R.drawable.ic_check_white_48dp),
            DEFAULT_TEXT_COLOR,
            SUCCESS_COLOR,
            duration,
            withIcon,
            true
        )
    }

    @Deprecated("使用ToastExt")
    fun error(message: String?) {
        error(appContext, message, Toast.LENGTH_SHORT, true)?.show()
    }
//===========================================使用ApplicationContext 方法=========================

    //*******************************************常规方法********************************************
    @Deprecated("使用ToastExt")
    fun error(message: String?, duration: Int) {
        error(appContext, message, duration, true)?.show()
    }

    fun error(message: String?, duration: Int, withIcon: Boolean): Toast? {
        return custom(
            appContext,
            message,
            getDrawable(appContext, R.drawable.ic_clear_white_48dp),
            DEFAULT_TEXT_COLOR,
            ERROR_COLOR,
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    fun normal(context: Context, message: String?): Toast? {
        return normal(context, message, Toast.LENGTH_SHORT, null, false)
    }

    @CheckResult
    fun normal(context: Context, message: String?, icon: Drawable): Toast? {
        return normal(context, message, Toast.LENGTH_SHORT, icon, true)
    }

    @CheckResult
    fun normal(context: Context, message: String?, duration: Int): Toast? {
        return normal(context, message, duration, null, false)
    }

    @CheckResult
    fun normal(context: Context, message: String?, duration: Int, icon: Drawable): Toast? {
        return normal(context, message, duration, icon, true)
    }

    @CheckResult
    fun normal(context: Context, message: String?, duration: Int, icon: Drawable?, withIcon: Boolean): Toast? {
        return custom(context, message, icon, DEFAULT_TEXT_COLOR, duration, withIcon)
    }

    @CheckResult
    fun warning(context: Context, message: String?): Toast? {
        return warning(context, message, Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    fun warning(context: Context, message: String?, duration: Int): Toast? {
        return warning(context, message, duration, true)
    }

    @CheckResult
    fun warning(context: Context, message: String?, duration: Int, withIcon: Boolean): Toast? {
        return custom(
            context,
            message,
            getDrawable(context, R.drawable.ic_error_outline_white_48dp),
            DEFAULT_TEXT_COLOR,
            WARNING_COLOR,
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    fun info(context: Context, message: String?): Toast? {
        return info(context, message, Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    fun info(context: Context, message: String?, duration: Int): Toast? {
        return info(context, message, duration, true)
    }

    @CheckResult
    fun info(context: Context, message: String?, duration: Int, withIcon: Boolean): Toast? {
        return custom(
            context,
            message,
            getDrawable(context, R.drawable.ic_info_outline_white_48dp),
            DEFAULT_TEXT_COLOR,
            INFO_COLOR,
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    fun success(context: Context, message: String?): Toast? {
        return success(context, message, Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    fun success(context: Context, message: String?, duration: Int): Toast? {
        return success(context, message, duration, true)
    }

    @CheckResult
    fun success(context: Context, message: String?, duration: Int, withIcon: Boolean): Toast? {
        return custom(
            context,
            message,
            getDrawable(context, R.drawable.ic_check_white_48dp),
            DEFAULT_TEXT_COLOR,
            SUCCESS_COLOR,
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    fun error(context: Context, message: String?): Toast? {
        return error(context, message, Toast.LENGTH_SHORT, true)
    }

//===========================================常规方法============================================

    @CheckResult
    fun error(context: Context, message: String?, duration: Int): Toast? {
        return error(context, message, duration, true)
    }

    @CheckResult
    fun error(context: Context, message: String?, duration: Int, withIcon: Boolean): Toast? {
        return custom(
            context,
            message,
            getDrawable(context, R.drawable.ic_clear_white_48dp),
            DEFAULT_TEXT_COLOR,
            ERROR_COLOR,
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    fun custom(context: Context, message: String?, icon: Drawable?, @ColorInt textColor: Int, duration: Int, withIcon: Boolean): Toast? {
        return custom(context, message, icon, textColor, -1, duration, withIcon, false)
    }

//*******************************************内需方法********************************************

    @CheckResult
    fun custom(
        context: Context,
        message: String?,
        @DrawableRes iconRes: Int,
        @ColorInt textColor: Int,
        @ColorInt tintColor: Int,
        duration: Int,
        withIcon: Boolean,
        shouldTint: Boolean
    ): Toast? {
        return custom(context, message, getDrawable(context, iconRes), textColor, tintColor, duration, withIcon, shouldTint)
    }

    @CheckResult
    fun custom(
        context: Context,
        message: String?,
        icon: Drawable?,
        @ColorInt textColor: Int,
        @ColorInt tintColor: Int,
        duration: Int,
        withIcon: Boolean,
        shouldTint: Boolean
    ): Toast? {
        if (message.isNullOrEmpty()) {
            return null
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return null
        }
        if (currentToast == null) {
            currentToast = Toast(context)
        } else {
            currentToast?.cancel()
            currentToast = null
            currentToast = Toast(context)
        }
        val toastLayout = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.toast_layout, null)
        val toastIcon = toastLayout.findViewById<ImageView>(R.id.toast_icon)
        val toastTextView = toastLayout.findViewById<TextView>(R.id.toast_text)
        val drawableFrame: Drawable?
        if (shouldTint) {
            drawableFrame = tint9PatchDrawableFrame(context, tintColor)
        } else {
            drawableFrame = getDrawable(context, R.drawable.toast_frame)
        }
        setBackground(toastLayout, drawableFrame)

        if (withIcon) {
            if (icon == null) {
                throw IllegalArgumentException("Avoid passing 'icon' as null if 'withIcon' is set to true")
            }
            setBackground(toastIcon, icon)
        } else {
            toastIcon.setVisibility(View.GONE)
        }
        toastTextView.setTextColor(textColor)
        toastTextView.setText(message)
        toastTextView.setTypeface(Typeface.create(TOAST_TYPEFACE, Typeface.NORMAL))
        currentToast!!.view = toastLayout
        currentToast!!.duration = duration
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            try {
                var mTNField = Toast::class.java.getDeclaredField("mTN")
                mTNField.isAccessible = true
                val mTN = mTNField.get(currentToast)

                var mHandlerField = mTNField.type.getDeclaredField("mHandler")
                mHandlerField.isAccessible = true
                val mHandler = mHandlerField.get(mTN) as Handler
                mHandlerField.set(mTN, object : Handler() {
                    override fun handleMessage(msg: Message) {
                        try {
                            mHandler.handleMessage(msg)
                        } catch (ignored: WindowManager.BadTokenException) {
                        }
                    }
                })
            } catch (ignored: IllegalAccessException) {
            } catch (ignored: NoSuchFieldException) {
            }

        }
        return currentToast!!
    }

    private fun tint9PatchDrawableFrame(context: Context, @ColorInt tintColor: Int): Drawable {
        val toastDrawable = getDrawable(context, R.drawable.toast_frame)!!
        if (toastDrawable is GradientDrawable || toastDrawable is NinePatchDrawable) {
            toastDrawable.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        }
        return toastDrawable
    }
//===========================================内需方法============================================


//******************************************系统 Toast 替代方法***************************************

    fun setBackground(view: View, drawable: Drawable?) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        view.background = drawable
//        } else {
//            view.setBackgroundDrawable(drawable)
//        }
    }

    fun getDrawable(context: Context, @DrawableRes id: Int): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getDrawable(id)
        } else {
            context.resources.getDrawable(id)
        }
    }

}