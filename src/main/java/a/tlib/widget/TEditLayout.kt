package a.tlib.widget

import android.content.Context
import android.util.AttributeSet
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes

/**
 * @author 田桂森 2021/5/10 0010
 * https://github.com/HITGIF/TextFieldBoxes/blob/master/README_CN.md
 */
class TEditLayout :TextFieldBoxes{
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}