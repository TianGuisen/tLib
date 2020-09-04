package a.tlib.utils.abs

import android.text.Editable
import android.text.TextWatcher

/**
 * @author 田桂森 2019/6/11
 */
abstract class TTextWatcher : TextWatcher {
    open override fun afterTextChanged(s: Editable) {

    }

    open override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    open override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }
}

