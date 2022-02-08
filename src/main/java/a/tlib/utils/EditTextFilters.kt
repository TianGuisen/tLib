package a.tlib.utils

import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import java.util.regex.Pattern

/**
 * 获取限制字符长度的filter
 */
fun getLimitsCharFilter(charLength: Int): InputFilter {
    return object : InputFilter {
        override fun filter(source: CharSequence?, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
            // 输入内容是否超过设定值
            if (getTextLength(dest.toString()) + getTextLength(source.toString()) > charLength) {
                // 输入框内已经有charLength个字符则返回空字符
                if (getTextLength(dest.toString()) >= charLength) {
                    return "";
                    // 如果输入框内没有字符，且输入的超过了charLength个字符，则截取前charLength/2个汉字
                } else if (getTextLength(dest.toString()) == 0) {
                    return source.toString().substring(0, charLength / 2)
                } else {
                    // 输入框已有的字符数为双数还是单数
                    if (getTextLength(dest.toString()) % 2 == 0) {
                        return source.toString().substring(0, charLength / 2 - (getTextLength(dest.toString()) / 2));
                    } else {
                        return source.toString().substring(0, charLength / 2 - (getTextLength(dest.toString()) / 2 + 1));
                    }
                }
            }
            return null;
        }
    }
}


/**
 * editText金额过滤器
 * android:inputType="numberDecimal"
 */
fun getMoneyInputFilter(): InputFilter {

    return object : InputFilter {
        //输入的最大金额
        private val MAX_VALUE = Integer.MAX_VALUE

        //小数点后的位数
        private val POINTER_LENGTH = 2
        private val POINTER = "."
        private val ZERO = "0"

        var mPattern = Pattern.compile("([0-9]|\\.)*")
        var mPattern2 = Pattern.compile("(\\.)*")

        /**
         * @param source 新输入的字符串
         * @param start  新输入的字符串起始下标，一般为0
         * @param end    新输入的字符串终点下标，一般为source长度-1
         * @param dest   输入之前文本框内容
         * @param dstart 原内容起始坐标，一般为0
         * @param dend   原内容终点坐标，一般为dest长度-1
         * @return 输入内容
         */
        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence {
            val sourceText = source.toString()
            val destText = dest.toString()
            //验证删除等按键  
            if (TextUtils.isEmpty(sourceText)) {
                return ""
            }
            val matcher = mPattern.matcher(source)
            //已经输入小数点的情况下，只能输入数字  
            if (destText.contains(POINTER)) {
                if (!matcher.matches()) {
                    return ""
                } else {
                    if (POINTER == source) {  //只能输入一个小数点
                        return ""
                    }
                }
                //验证小数点精度，保证小数点后只能输入两位  
                val index = destText.indexOf(POINTER)
                val length = destText.length - index
                //如果长度大于2，并且新插入字符在小数点之后  
                if (length > POINTER_LENGTH && index < dstart) {
                    //超出2位返回null  
                    return ""
                }
            } else {
                //没有输入小数点的情况下，只能输入小数点和数字，但首位不能输入小数点  
                if (!matcher.matches()) {
                    return ""
                } else {
                    if (POINTER == source && TextUtils.isEmpty(destText)) {
                        return ""
                    }
                }
            }
            //验证输入金额的大小  
            val sumText = java.lang.Double.parseDouble(destText + sourceText)
            if (sumText > MAX_VALUE) {
                return dest.subSequence(dstart, dend)
            }
            //验证0开头不能再输入数字
            return if ("0" == destText && "." != source) {
                ""
            } else dest.subSequence(dstart, dend).toString() + sourceText
        }
    }
}

/**
 * 获取字符数量 汉字占2个，英文占一个
 *
 * @param text
 * @return
 */
private fun getTextLength(text: String): Int {
    var length = 0
    for (i in 0 until text.length) {
        if (text[i].toInt() > 255) {
            length += 2
        } else {
            length++
        }
    }
    return length
}