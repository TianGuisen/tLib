package a.tlib.widget.dialog.commonDialog


import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import a.tlib.R
import a.tlib.utils.getcolor
import a.tlib.utils.string
import a.tlib.widget.dialog.baseDialog.BaseTDialog
import a.tlib.widget.dialog.baseDialog.ViewHandlerListener
import a.tlib.widget.dialog.baseDialog.ViewHolder
import com.ruffian.library.widget.REditText

/**
 * edittext  dialog
 * 单按钮，setSinge
 * 双按钮, setLeft或者setRight
 * 获取editText设置相关属性
 */
class EditDialog : BaseTDialog<EditDialog>() {
    companion object {
        fun init(fragmentManager: androidx.fragment.app.FragmentManager?): EditDialog {
            return EditDialog().apply {
                setFragmentManager(fragmentManager)
            }
        }
    }

    override var layoutId: Int = R.layout.dialog_edit

    /**
     * 获取editText设置相关属性
     */
    public lateinit var editText: REditText
    private var isShowSinge = false
    private var maxEdtCont = 10
    private var inputType = 0
    private var titleText: CharSequence = "温馨提示"

    private var hintEdt: CharSequence = "请输入"
    private var leftText: CharSequence = "取消"
    private var leftClickListener: ((View) -> Unit)? = null
    private var leftColor: Int = getcolor(R.color.color_9b)

    private var rightText: CharSequence = "确定"
    private var rightClickListener: ((String) -> Unit)? = null
    private var rightColor: Int = getcolor(R.color.color_FFA108)


    private var singeText: CharSequence = "确定"
    private var singeClickListener: ((String) -> Unit)? = null
    private var singeColor: Int = getcolor(R.color.color_FFA108)

    /**
     * View Handler
     * The management of the relevant state of the view is written here
     */
    override fun viewHandler(): ViewHandlerListener? {
        return object : ViewHandlerListener() {
            override fun convertView(holder: ViewHolder, dialog: BaseTDialog<*>) {
                holder.getView<TextView>(R.id.tv_title).text = titleText

                editText = holder.getView<REditText>(R.id.ed_msg)
                editText.hint = hintEdt

                editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxEdtCont))
                when (inputType) {
                    1 -> editText.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL)
                }

                holder.getView<LinearLayout>(R.id.ll_double).visibility = if (isShowSinge) View.GONE else View.VISIBLE



                holder.getView<TextView>(R.id.tv_left).apply {
                    text = leftText
                    setTextColor(leftColor)
                    setOnClickListener {
                        leftClickListener?.invoke(it)
                        dialog.dismiss()
                    }
                }

                holder.getView<TextView>(R.id.tv_right).apply {
                    text = rightText
                    setTextColor(rightColor)
                    setOnClickListener {
                        rightClickListener?.invoke(editText.string)
                        dialog.dismiss()
                    }
                }

                holder.getView<TextView>(R.id.tv_singe).apply {
                    visibility = if (isShowSinge) View.VISIBLE else View.GONE
                    text = singeText
                    setTextColor(singeColor)
                    setOnClickListener {
                        singeClickListener?.invoke(editText.string)
                        dialog.dismiss()
                    }
                }
            }
        }
    }

    override fun layoutView(): View? = null

    /**
     * Title Text(Support Rich text)
     */
    fun setTitle(title: CharSequence): EditDialog {
        titleText = title
        return this
    }

    /**
     * 设置输入框中隐藏的内容
     */
    fun setHint(hintEdtInfo: String): EditDialog {
        hintEdt = hintEdtInfo
        return this
    }

    /**
     * 输入最大长度
     */
    fun setMaxEdtNo(maxEdtCont: Int): EditDialog {
        this.maxEdtCont = maxEdtCont
        return this
    }

    /**
     *  1是纯数字
     */
    fun setInputType(inputType: Int): EditDialog {
        this.inputType = inputType
        return this
    }

    /**
     * 左按钮
     */
    @JvmOverloads
    fun setleft(text: CharSequence,
                listener: ((v: View) -> Unit)? = null,
                @ColorInt color: Int = leftColor): EditDialog {
        leftText = text
        leftClickListener = listener
        leftColor = color
        return this
    }

    /**
     * 右按钮
     */
    @JvmOverloads
    fun setRight(text: CharSequence,
                 listener: ((string: String) -> Unit)? = null,
                 @ColorInt color: Int = rightColor): EditDialog {
        rightText = text
        rightClickListener = listener
        rightColor = color
        return this
    }


    /**
     *单按钮
     */
    @JvmOverloads
    fun setSinge(text: CharSequence,
                 listener: ((string: String) -> Unit)? = null,
                 @ColorInt color: Int = rightColor): EditDialog {
        isShowSinge = true
        singeText = text
        singeClickListener = listener
        singeColor = color
        return this
    }

    override fun initView(view: View) {

    }

}



