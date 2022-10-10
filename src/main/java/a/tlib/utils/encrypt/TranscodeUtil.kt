package a.tlib.utils.encrypt

/**
 * @author 田桂森 2020/3/24
 * 转码相关
 */
object TranscodeUtil {
    //Unicode转中文方法
    @JvmStatic
    fun unicodeToCn(unicode: String): String {
        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格 */
        val strs = unicode.split("\\\\u".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var returnStr = ""
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (i in 1 until strs.size) {
            returnStr += Integer.valueOf(strs[i], 16).toInt().toChar()
        }
        return returnStr
    }

    //中文转Unicode
    @JvmStatic
    fun cnToUnicode(cn: String): String {
        val chars = cn.toCharArray()
        var returnStr = ""
        for (i in chars.indices) {
            returnStr += "\\u" + Integer.toString(chars[i].toInt(), 16)
        }
        return returnStr
    }
}