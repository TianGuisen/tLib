package a.tlib.utils

import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import java.math.BigDecimal
import java.text.DecimalFormat


/**
 * @author 田桂森 2019/12/31 0031
 */


/**
 *  不为null且不是“”
 */
inline fun CharSequence?.isNotNullEmpty(): Boolean = !this.isNullOrEmpty()

/**
 *  不为“”
 */
inline fun CharSequence.isNotEmpty(function: (String) -> Unit) = (length > 0).yes { function(this.toString()) }

/**
 *  不为null且不是“”
 */
inline fun CharSequence?.isNotNullEmpty(function: (String) -> Unit) = isNullOrEmpty().no { function(this!!.toString()) }

/**
 *  是null或者“”
 */
inline fun CharSequence?.isNullOrEmpty(function: () -> Unit) = isNullOrEmpty().yes(function)

/**
 *  不为null且不是“”和“     ”
 */
fun CharSequence?.isNotNullBlank(): Boolean = !this.isNullOrBlank()

/**
 *  不为null且不是“”和“     ”
 */
inline fun CharSequence?.isNotNullBlank(function: (String) -> Unit) = isNullOrBlank().no { function(this!!.toString()) }

/**
 *  是null或者“”或者“     ”
 */
inline fun CharSequence?.isNullOrBlank(function: () -> Unit) = isNullOrBlank().yes(function)

/**
 * 获取最后位置的字符串
 */
fun CharSequence.lastStr(): String = this.get(this.length - 1).toString()

/**
 * 获取从i开始到结束的字符串
 */
fun CharSequence.subEnd(i: Int) = this.substring(this.length - i)

/**
 * 获取前面i个字符串
 */
fun CharSequence.subStart(i: Int) = this.substring(0, i)

/**
 * 删除前面i位,默认为1
 */
fun CharSequence.delFirst(i: Int = 1): String {
    try {
        return this.substring(i, this.length)
    } catch (a: Exception) {
        return ""
    }
}

/**
 * 删除最后i位,默认为1
 */
fun CharSequence.delLast(i: Int = 1): String {
    try {
        return this.substring(0, this.length - i)
    } catch (a: Exception) {
        return ""
    }
}

/**
 *string转double,null会转成0
 */
fun String?.double(): Double {
    val toDouble = this?.toDoubleOrNull()
    if (toDouble == null) {
        return 0.0
    } else {
        return toDouble
    }
}

/**
 *string转double,null会转成0
 */
fun String?.float(): Float {
    val tofloat = this?.toFloatOrNull()
    if (tofloat == null) {
        return 0.0f
    } else {
        return tofloat
    }
}

/**
 *string转bigDecimal,null会转成0
 */
fun String?.bigDecimal(): BigDecimal {
    if (isNullOrEmpty()) {
        BigDecimal(0)
    }
    return BigDecimal(this)

}

/**
 *string转int,null会转成0
 */
fun String?.int(): Int {
    val toInt = this?.double()?.toInt()
    if (toInt == null) {
        return 0
    } else {
        return toInt
    }
}

/**
 *string转int,null会转成0
 */
fun String?.long(): Long {
    val toLong = this?.toLong()
    if (toLong == null) {
        return 0
    } else {
        return toLong
    }
}

/**
 * 限制最大最小值
 */
inline fun <reified T:Number> T.limitMinMax(mix: Double? = null, max: Double? = null): T {
    if (mix != null && this.toDouble() < mix) {
        return mix as T
    } else if (max != null && this.toDouble()  > max) {
        return max as T
    }
    return this
}

/**
 * 对字符加星号处理：除前面几位和后面几位外，其他的字符以星号代替
 *
 * @param frontNum 保留前面字符的位数
 * @param endNum   保留后面字符的位数
 */
fun String.getStarStr(frontNum: Int, endNum: Int): String {

    if (frontNum >= this.length || frontNum < 0) {
        return this
    }
    if (endNum >= this.length || endNum < 0) {
        return this
    }
    if (frontNum + endNum >= this.length) {
        return this
    }
    var starStr = ""
    for (i in 0 until this.length - frontNum - endNum) {
        starStr = "$starStr*"
    }
    return (this.substring(0, frontNum) + starStr + this.substring(this.length - endNum, this.length))
}

/**
 * 处理文本，将文本位数限制为maxLen，中文两个字符，英文一个字符
 *
 * * @param content 要处理的文本
 * @param maxLen  限制文本字符数，中文两个字符，英文一个字符。例如：a啊b吧，则maxLen为6
 * @return
 */
fun String.handleText(maxLen: Int): String {
    if (this == null || this.isBlank()) {
        return ""
    }
    var count = 0
    var endIndex = 0
    for (i in 0 until this.length) {
        val item = this[i]
        count = if (item.toInt() < 128) {
            count + 1
        } else {
            count + 2
        }
        if (maxLen == count || item.toInt() >= 128 && maxLen + 1 == count) {
            endIndex = i
        }
    }
    return if (count <= maxLen) {
        this
    } else {
        return this.substring(0, endIndex) + "...";//末尾添加省略号
//        content.substring(0, endIndex + 1) //末尾不添加省略号
    }
}

/**
 * 转为保留2位小数
 */
fun String.toMoney(): String {
    val a: String = try {
        val format = DecimalFormat("0.00")
        format.format(BigDecimal(this))
    } catch (e: Exception) {
        return "0.00"
    }
    return a
}

/**
 * 转为保留2位小数
 */
fun Double.toMoney(): String {
    val a: String = try {
        val format = DecimalFormat("0.00")
        format.format(BigDecimal(this))
    } catch (e: Exception) {
        return "0.00"
    }
    return a
}

/**
 * 转为保留1位小数
 */
fun String.decimals1(): String {
    val a: String = try {
        val format = DecimalFormat("0.0")
        format.format(BigDecimal(this))
    } catch (e: Exception) {
        return "0.0"
    }
    return a
}

/**
 * 缩小第一位的字
 * @param value
 */
fun CharSequence.zoomSmallFirst(size: Float = 0.7f): SpannableString {
    var spannableString = SpannableString(this)
    if (isNotEmpty()) {
        spannableString.setSpan(RelativeSizeSpan(size), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return spannableString
}

/**
 * 缩放中间start-end位置的字
 * @param value
 */
fun String.zoomStartEnd(start: Int, end: Int, size: Float = 1.5f): SpannableString {
    var spannableString = SpannableString(this)
    if (isNotEmpty()) {
        if (start >= 0 && spannableString.length >= end)
            spannableString.setSpan(RelativeSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return spannableString
}

/**
 * 缩放小数点前的字，默认放大1.5倍
 * @param value
 */
fun CharSequence.zoomPointBefor(rate: Float = 1.5f): SpannableString {
    var spannableString = SpannableString(this)
    if (isNotEmpty()) {
        if (contains(".")) {
            spannableString.setSpan(RelativeSizeSpan(rate), 0, indexOf("."), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
    return spannableString
}

/**
 * 缩放前面的X个字Y倍，默认放大第一个字1.5位
 * @param value
 */
fun CharSequence.zoomStart(x: Int = 1, rate: Float = 1.5f): SpannableString {
    var spannableString = SpannableString(this)
    if (isNotEmpty()) {
        spannableString.setSpan(RelativeSizeSpan(rate), 0, x, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return spannableString
}

/**
 * 截取分隔字符串之前的字符串，不包括分隔字符串<br>
 * 如果给定的字符串为空串（null或""）或者分隔字符串为null，返回原字符串<br>
 * 如果分隔字符串未找到，返回原字符串，举例如下：
 * <pre>
 * StrUtil.subBefore(null, *)      = null
 * StrUtil.subBefore("", *)        = ""
 * StrUtil.subBefore("abc", 'a')   = ""
 * StrUtil.subBefore("abcba", 'b') = "a"
 * StrUtil.subBefore("abc", 'c')   = "ab"
 * StrUtil.subBefore("abc", 'd')   = "abc"
 * </pre>
 * @param string 被查找的字符串
 * @param separator 分隔字符串（不包括）
 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
 * @return 切割后的字符串
 */
fun CharSequence?.subBefore(separator: String, isLastSeparator: Boolean = true): String {
    if (this.isNullOrEmpty()) {
        return ""
    }
    val pos = if (isLastSeparator) this.lastIndexOf(separator) else this.indexOf(separator)
    if (-1 == pos) {
        return this.toString();
    }
    if (0 == pos) {
        return "";
    }
    return this.substring(0, pos);
}

/**
 * 截取分隔字符串之后的字符串，不包括分隔字符串<br>
 * 如果给定的字符串为空串（null或""），返回原字符串<br>
 * 如果分隔字符串为空串（null或""），则返回空串，如果分隔字符串未找到，返回空串，举例如下：
 * StrUtil.subAfter(null, *)      = null
 * StrUtil.subAfter("", *)        = ""
 * StrUtil.subAfter(*, null)      = ""
 * StrUtil.subAfter("abc", "a")   = "bc"
 * StrUtil.subAfter("abcba", "b") = "cba"
 * StrUtil.subAfter("abc", "c")   = ""
 * StrUtil.subAfter("abc", "d")   = ""
 * StrUtil.subAfter("abc", "")    = "abc"
 * @param string 被查找的字符串
 * @param separator 分隔字符串（不包括）
 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
 * @return 切割后的字符串
 */
fun CharSequence?.subAfter(separator: String, isLastSeparator: Boolean = true): String {
    if (this.isNullOrEmpty()) {
        return ""
    }
    val str = this.toString()
    val sep = separator.toString()
    val pos = if (isLastSeparator) str.lastIndexOf(sep) else str.indexOf(sep)
    return if (-1 == pos || this.length - 1 == pos) {
        ""
    } else str.substring(pos + separator.length)
}

/***
 * 获取url 指定name的value;
 */
fun CharSequence.getUrlValueByName(name: String): String {
    var result = ""
    val index = this.indexOf("?")
    val temp = this.substring(index + 1)
    val keyValue = temp.split("&".toRegex()).toTypedArray()
    for (str in keyValue) {
        if (str.contains(name)) {
            result = str.replace("$name=", "")
            break
        }
    }
    return result
}

/**
 * 不够位数的在前面补0，保留num的长度位数字
 */
fun CharSequence.fill0(num: Int,tag:String="0"): String {
    var cs = ""
    if (this.length < num) {
        for (i in 0 until length - this.length) {
            cs = tag+cs
        }
        return cs + this
    }
    return this.toString()
}

/**
 * 不够位数的在前面补0，保留num的长度位数字
 */
fun Number.fill0(num: Int): String {
    return String.format("%0" + num + "d", this)
}