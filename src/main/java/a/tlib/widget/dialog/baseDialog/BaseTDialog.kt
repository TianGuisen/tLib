package a.tlib.widget.dialog.baseDialog

import a.tlib.R
import a.tlib.utils.AutoSizeUtil
import a.tlib.utils.KeyBoardUtil
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import a.tlib.logger.YLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import me.jessyan.autosize.internal.CustomAdapt


/**
 * BaseDialog(Can inherit this class)
 * Date 2018/6/26
 * @author limuyang
 * https://github.com/limuyang2/LDialog/blob/master/README_CN.md
 *
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseTDialog<T : BaseTDialog<T>> : DialogFragment(), CustomAdapt {
    val TAG = this.javaClass.simpleName
    protected var baseParams: BaseDialogParams

    protected var viewHandlerListener: ViewHandlerListener?

    private var onDialogDismissListener: ((DialogInterface) -> Unit)? = null

    lateinit var mContext: Context
    lateinit var act: FragmentActivity

    init {
        baseParams = BaseDialogParams().apply {
            view = layoutView()
        }
        viewHandlerListener = this.viewHandler()
    }

    abstract val layoutId: Int   // 布局文件

    protected open fun layoutView(): View? = null

    /**
     * 如果【需要】考虑横竖屏旋转，则控件的相关属性在此设置
     */
    open fun viewHandler(): ViewHandlerListener? = null

    /**
     * 如果不考虑横竖屏旋转，使用这个,在viewHandler()之后执行
     */
    protected abstract fun initView(view: View)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //全屏
        //Restore UI status
//        savedInstanceState?.let {
//            baseParams = it.getParcelable(KEY_PARAMS)
//            viewHandlerListener = savedInstanceState.getParcelable(KEY_VIEW_HANDLER)
//            onDialogDismissListener = savedInstanceState.getParcelable(KEY_DISMISS_LISTENER)
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.d("onCreateDia", this.javaClass.simpleName)//用于快速定位页面对应的代码
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        act = activity!!
//        AutoSize.autoConvertDensity(getActivity(), 1080f, false);
        return when {
            layoutId > 0 -> inflater.inflate(layoutId, container)
            baseParams.view != null -> baseParams.view!!
            else ->
                throw IllegalArgumentException("请先设置LayoutRes或View!")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImmersionBar()
        viewHandlerListener?.convertView(ViewHolder.create(view), this)
        initView(view)

        //Set open Keyboard
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT && baseParams.needKeyboardViewId != 0) {
            val editText = view.findViewById<EditText>(baseParams.needKeyboardViewId)

            editText.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                            ?: return
                    editText.isFocusable = true
                    editText.isFocusableInTouchMode = true
                    editText.requestFocus()
                    if (imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            editText.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }
                    }
                }
            })
        }
    }

    /**
     * 初始化沉浸式
     */
    open protected fun initImmersionBar() {
//        ImmersionBar.with(this)
//                .navigationBarColor(R.color.black)
//                .navigationBarDarkIcon(false)
//                .autoNavigationBarDarkModeEnable(true)
//                .init()
    }

    //save UI state
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.apply {
//            putParcelable(KEY_PARAMS, baseParams)
//            putParcelable(KEY_VIEW_HANDLER, viewHandlerListener)
//            putParcelable(KEY_DISMISS_LISTENER, onDialogDismissListener)
//        }
    }

    override fun onStart() {
        super.onStart()
        //Get screen size
        val point = Point()
        val windowManager = activity?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        windowManager?.defaultDisplay?.getSize(point)

        //Set window
        dialog?.window?.let {
            val params = it.attributes
            params.gravity = baseParams.gravity
            it.attributes
            //Set dialog width
            when {
                baseParams.widthDp > 0f -> params.width = dp2px(mContext, baseParams.widthDp)
                baseParams.widthPt > 0f -> params.width = AutoSizeUtil.pt2px(baseParams.widthPt)
                baseParams.widthScale > 0f -> {
                    if ((this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE && baseParams.keepWidthScale)
                            || this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
                    ) {
                        //横屏并且保持比例 或者 竖屏
                        params.width = (point.x * baseParams.widthScale).toInt()
                    }
                }
            }

            //Set dialog height
            when {
                baseParams.heightDp > 0f -> params.height = dp2px(mContext, baseParams.heightDp)
                baseParams.heightPt > 0f -> params.height = AutoSizeUtil.pt2px(baseParams.heightPt)
                baseParams.heightScale > 0f -> {
                    if ((this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE && baseParams.keepHeightScale)
                            || this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
                    ) {
                        //横屏并且保持比例 或者 竖屏
                        params.height = (point.y * baseParams.heightScale).toInt()
                    }
                }
            }
            it.attributes = params
            //处理底部弹出时候，遮挡导航栏的问题
//            if (params.gravity == Gravity.BOTTOM) {
//                view?.post {
//                    dialog?.window?.apply {
//                        val lp = attributes.apply {
//                            height = view!!.measuredHeight + getNavigationBarHeight(act)
//                        }
//                        attributes = lp
//                    }
//                }
//            }
            if (baseParams.backgroundDrawableRes == 0) {
                it.setBackgroundDrawable(null)
            } else {
                it.setBackgroundDrawableResource(baseParams.backgroundDrawableRes)
            }
            it.setWindowAnimations(baseParams.animStyle)
            if (baseParams.backgroundLight) {
                params.dimAmount = 0f
                params.flags = params.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
                it.attributes = params
            }
        }

        //Set touch cancelable
        if (!baseParams.cancelable) {
            isCancelable = baseParams.cancelable
        } else {
            dialog?.setCanceledOnTouchOutside(baseParams.cancelableOutside)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (baseParams.needKeyboardViewId != 0) {
            val editText = view?.findViewById<EditText>(baseParams.needKeyboardViewId)
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    ?: return
            imm.hideSoftInputFromWindow(editText?.windowToken, 0)
        }
        super.onDismiss(dialog)
        onDialogDismissListener?.invoke(dialog)
    }


    protected fun setFragmentManager(fragmentManager: androidx.fragment.app.FragmentManager?): T {
        baseParams.fragmentManager = fragmentManager
        return this as T
    }

    /*** Set Params  (start) [External call]***/
    fun setTag(tag: String): T {
        baseParams.tag = tag
        return this as T
    }

    fun setDismissListener(onDialogDismissListener: (DialogInterface) -> Unit): T {
        this.onDialogDismissListener = onDialogDismissListener
        return this as T
    }

    fun setGravity(gravity: Int): T {
        baseParams.gravity = gravity
        return this as T
    }

    /**
     * 设置宽度百分比
     */
    fun setWidthScale(@FloatRange(from = 0.0, to = 1.0) scale: Float): T {
        baseParams.widthScale = scale
        return this as T
    }

    fun setWidthDp(dp: Float): T {
        baseParams.widthDp = dp
        return this as T
    }

    fun setWidthPt(pt: Float): T {
        baseParams.widthPt = pt
        return this as T
    }

    fun setWidthHeightPt(width: Int, height: Int): T {
        baseParams.widthPt = width.toFloat()
        baseParams.heightPt = height.toFloat()
        return this as T
    }

    fun setHeightScale(@FloatRange(from = 0.0, to = 1.0) scale: Float): T {
        baseParams.heightScale = scale
        return this as T
    }

    fun setHeightDp(dp: Float): T {
        baseParams.heightDp = dp
        return this as T
    }

    fun setHeightPt(pt: Float): T {
        baseParams.heightPt = pt
        return this as T
    }

    /**
     * 设置横竖屏宽度都保持比例
     */
    fun setKeepWidthScale(isKeep: Boolean): T {
        baseParams.keepWidthScale = isKeep
        return this as T
    }

    /**
     * 设置横竖屏高度都保持比例
     */
    fun setKeepHeightScale(isKeep: Boolean): T {
        baseParams.keepHeightScale = isKeep
        return this as T
    }

    /**
     * false,点击屏幕或物理返回键，dialog不消失
     */
    fun setCancelableAll(cancelable: Boolean = false): T {
        baseParams.cancelable = cancelable
        return this as T
    }

    /**
     * false,点击屏幕：dialog不消失；点击返回键：dialog消失
     */
    fun setCancelableOutside(cancelableOutside: Boolean = false): T {
        baseParams.cancelableOutside = cancelableOutside
        return this as T
    }

    /**
     * 0是透明
     */
    fun setBackgroundDrawableRes(@DrawableRes resId: Int): T {
        baseParams.backgroundDrawableRes = resId
        return this as T
    }

    fun setAnimStyle(@StyleRes animStyleRes: Int): T {
        baseParams.animStyle = animStyleRes
        return this as T
    }

    /**
     * 设置为常用的底部弹出
     */
    fun setBottomStyle() {
        setWidthScale(1.0f)
        setBackgroundDrawableRes(R.drawable.dialog_bg_bottom_white)
        setGravity(Gravity.BOTTOM)
        setAnimStyle(R.style.DiaAnimBottom)
    }

    /**
     * 背景亮，可以在init中写，也可以在initView中写
     */
    fun setBackgroundLight(): T {
        baseParams.backgroundLight = true
        dialog?.window?.let {
            val attributes = it.attributes
            it.attributes = attributes
        }
        return this as T
    }

    /**
     * auto open keyboard, (only EditText)
     * @param id Int EditTextView ID
     * @return T
     */
    fun setNeedKeyboardEditTextId(@IdRes id: Int): T {
        baseParams.needKeyboardViewId = id
        return this as T
    }

    fun show(): T {
        return show(baseParams.fragmentManager)
    }

    open fun show(fm: FragmentManager?): T {
        fm?.let {
            showTag(it)
        }
        return this as T
    }

    fun show(fra: Fragment?): T {
        fra?.fragmentManager?.let {
            showTag(it)
        }
        return this as T
    }

    fun show(act: FragmentActivity?): T {
        act?.supportFragmentManager?.let {
            showTag(it)
        }
        return this as T
    }

    fun showTag(manager: FragmentManager) {
        try {
            val mDismissed = DialogFragment::class.java.getDeclaredField("mDismissed")
            mDismissed.isAccessible = true
            mDismissed.set(this, false)
            val mShownByMe = DialogFragment::class.java.getDeclaredField("mShownByMe")
            mShownByMe.isAccessible = true
            mShownByMe.set(this, true)
            manager.beginTransaction()
                    .add(this, tag)
                    .commitAllowingStateLoss()
        } catch (e: Exception) {
            YLog.d(e)
        }
    }

    override fun onDestroyView() {
        KeyBoardUtil.hideSoftKeyboard(act)
        super.onDestroyView()
    }

    override fun isBaseOnWidth(): Boolean {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
    }

    override fun getSizeInDp(): Float {
        return 1080f
    }

    companion object {
        private const val KEY_PARAMS = "key_params"
        private const val KEY_VIEW_HANDLER = "view_handler"
        private const val KEY_DISMISS_LISTENER = "dismiss_listener"

        private fun dp2px(context: Context, dipValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dipValue * scale + 0.5f).toInt()
        }

    }

    abstract class UnParcelableParams(
            var fragmentManager: androidx.fragment.app.FragmentManager? = null,
            var view: View? = null
    )

    class BaseDialogParams(
            var widthScale: Float = 0.85f,
            var widthDp: Float = 0f,
            var widthPt: Float = 0f,

            var heightScale: Float = 0f,
            var heightDp: Float = 0f,
            var heightPt: Float = 0f,
            var keepWidthScale: Boolean = false,
            var keepHeightScale: Boolean = false,
            var gravity: Int = Gravity.CENTER,
            var tag: String = "TDialog",
            var cancelable: Boolean = true,
            var cancelableOutside: Boolean = true,
            var backgroundDrawableRes: Int = R.drawable.dialog_bg_def,
            var animStyle: Int = 0,
            var needKeyboardViewId: Int = 0,
            var backgroundLight: Boolean = false
    ) : UnParcelableParams()

    override fun dismiss() {
        try {
            super.dismiss()
        } catch (e: Exception) {
        }
    }
}