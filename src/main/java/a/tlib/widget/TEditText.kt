package a.tlib.widget

import a.tlib.utils.KeyBoardUtil
import android.content.Context
import android.util.AttributeSet
import studio.carbonylgroup.textfieldboxes.ExtendedEditText

/**
 * @author 田桂森 2021/5/10 0010
 * https://github.com/HITGIF/TextFieldBoxes/blob/master/README_CN.md
 */
class TEditText : ExtendedEditText {
    init {
        setOnFocusChangeListener { view, b ->
            handKeyBoard(b)
        }
    }

    @Deprecated("使用这个会有键盘弹出bug，使用下面那个")
    override fun setOnFocusChangeListener(l: OnFocusChangeListener) {
        super.setOnFocusChangeListener(l)
    }

    fun setOnFocusChangeListener(fun1: ((Boolean) -> Unit)) {
        setOnFocusChangeListener { view, b ->
            handKeyBoard(b)
            fun1(b)
        }
    }

    fun handKeyBoard(b: Boolean) {
        if (b) {
            KeyBoardUtil.showSoftKeyboard(this)
        }
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
}