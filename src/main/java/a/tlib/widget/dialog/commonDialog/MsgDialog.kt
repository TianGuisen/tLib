package a.tlib.widget.dialog.commonDialog


import a.tlib.R
import a.tlib.utils.AppUtil
import a.tlib.utils.getcolor
import a.tlib.widget.dialog.baseDialog.BaseTDialog
import a.tlib.widget.dialog.baseDialog.ViewHandlerListener
import a.tlib.widget.dialog.baseDialog.ViewHolder
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.fragment.app.FragmentManager

/**
 * 普通的msg dialog
 * 单按钮，setSinge
 * 双按钮, setLeft或者setRight
 */
open class MsgDialog : BaseTDialog<MsgDialog>() {
    companion object {
        fun init(fragmentManager: FragmentManager?): MsgDialog {
            return MsgDialog().apply {
                setFragmentManager(fragmentManager)
            }
        }
    }
    init {
        if (AppUtil.isHorizontalScreen()){
            setWidthScale(0.5f)
        }
    }
    override var layoutId = R.layout.dialog_msg

    private var isShowSinge = false

    private var titleText: CharSequence = "温馨提示"

    private var msgText: CharSequence = ""
    var tv_title: TextView? = null
    var tv_msg: TextView? = null

    private var msgSubText: CharSequence = ""
    public var tv_msg_sub: TextView? = null

    private var leftText: CharSequence = "取消"
    private var leftClickListener: ((View) -> Unit)? = null
    private var leftColor: Int = getcolor(R.color.color_9b)
    private var leftDismis: Boolean = true

    private var rightText: CharSequence = "确定"
    private var rightClickListener: ((View) -> Unit)? = null
    private var rightColor: Int = getcolor(R.color.color_FFA108)
    private var rightDismis: Boolean = true


    private var singeText: CharSequence = "确定"
    private var singeClickListener: ((View) -> Unit)? = null
    private var singeColor: Int = getcolor(R.color.color_FFA108)
    private var singeDismis: Boolean = true

    /**
     * View Handler
     * The management of the relevant state of the view is written here
     */
    override fun viewHandler(): ViewHandlerListener? {
        return object : ViewHandlerListener() {
            override fun convertView(holder: ViewHolder, dialog: BaseTDialog<*>) {
                tv_title = holder.getView<TextView>(R.id.tv_title).apply {
                    text = titleText
                }

                tv_msg = holder.getView<TextView>(R.id.tv_msg).apply {
                    if (msgText.isNotEmpty()) {
                        text = msgText
                        visibility = View.VISIBLE
                        setMovementMethod(LinkMovementMethod.getInstance())
                    }
                }

                tv_msg_sub = holder.getView<TextView>(R.id.tv_msg_sub).apply {
                    if (msgSubText.isNotEmpty()) {
                        setMovementMethod(LinkMovementMethod.getInstance())
                        visibility = View.VISIBLE
                        text = msgSubText
                    }
                }

                holder.getView<LinearLayout>(R.id.ll_double).visibility = if (isShowSinge) View.GONE else View.VISIBLE

                holder.getView<TextView>(R.id.tv_left).apply {
                    text = leftText
                    setTextColor(leftColor)
                    setOnClickListener {
                        if (leftDismis) dialog.dismiss()
                        leftClickListener?.invoke(it)
                    }
                }

                holder.getView<TextView>(R.id.tv_right).apply {
                    text = rightText
                    setTextColor(rightColor)
                    setOnClickListener {
                        if (rightDismis) dialog.dismiss()
                        rightClickListener?.invoke(it)
                    }
                }

                holder.getView<TextView>(R.id.tv_singe).apply {
                    visibility = if (isShowSinge) View.VISIBLE else View.GONE
                    text = singeText
                    setTextColor(singeColor)
                    setOnClickListener {
                        if (singeDismis) dialog.dismiss()
                        singeClickListener?.invoke(it)
                    }
                }
            }
        }
    }


    override fun layoutView(): View? = null


    /**
     * Title Text(Support Rich text)
     */
    fun setTitle(title: CharSequence): MsgDialog {
        titleText = title
        return this
    }

    /**
     * Message Text(Support Rich text)
     */
    fun setMsg(msg: CharSequence): MsgDialog {
        msgText = msg
        return this
    }

    /**
     * Message Text(Support Rich text)
     */
    fun setMsgSub(msg: CharSequence): MsgDialog {
        msgSubText = msg
        return this
    }

    /**
     * 左按钮
     */
    @JvmOverloads
    fun setLeft(text: CharSequence,
                listener: ((View) -> Unit)? = null,
                dismis: Boolean = true,
                @ColorInt color: Int = leftColor): MsgDialog {
        leftText = text
        leftClickListener = listener
        leftColor = color
        leftDismis = dismis
        return this
    }

    /**
     * 右按钮
     */
    @JvmOverloads
    fun setRight(text: CharSequence,
                 listener: ((View) -> Unit)? = null,
                 dismis: Boolean = true,
                 @ColorInt color: Int = rightColor): MsgDialog {
        rightText = text
        rightClickListener = listener
        rightColor = color
        rightDismis = dismis
        return this
    }


    /**
     *单按钮
     */
    @JvmOverloads
    fun setSinge(text: CharSequence,
                 listener: ((View) -> Unit)? = null,
                 dismis: Boolean = true,
                 @ColorInt color: Int = rightColor): MsgDialog {
        isShowSinge = true
        singeText = text
        singeClickListener = listener
        singeColor = color
        singeDismis = dismis
        return this
    }

    override fun initView(view: View) {

    }

}



