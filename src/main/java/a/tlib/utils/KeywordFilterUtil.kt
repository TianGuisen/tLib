package a.tlib.utils

import java.util.regex.Pattern

/**
 * @author 田桂森 2020/3/19
 * 关键字过滤
 */
object KeywordFilterUtil {
    /**
     * 过滤直播消息
     */
    @JvmStatic
    fun filterLiveMsg(str: String): String {
        var strBuilder = StringBuilder(str)
        var indexOf = str.indexOf("微信")
        if (indexOf != -1) {
            strBuilder.replace(indexOf, indexOf + 2, "**")
        }
        var upperStr = str.toUpperCase()
        indexOf = upperStr.indexOf("WX")
        if (indexOf != -1) {
            strBuilder.replace(indexOf, indexOf + 2, "***")
        }
        indexOf = upperStr.indexOf("WEIXIN")
        if (indexOf != -1) {
            strBuilder.replace(indexOf, indexOf + 6, "******")
        }
        indexOf = upperStr.indexOf("WEIX")
        if (indexOf != -1) {
            strBuilder.replace(indexOf, indexOf + 4, "****")
        }
        indexOf = upperStr.indexOf("WXIN")
        if (indexOf != -1) {
            strBuilder.replace(indexOf, indexOf + 4, "****")
        }
        indexOf = upperStr.indexOf("QQ")
        if (indexOf != -1) {
            strBuilder.replace(indexOf, indexOf + 2, "**")
        }


        var tmpWX = StringBuilder()
        var tmpNum = StringBuilder()
        for (c in strBuilder.toString().toCharArray()) {
            //收集微信的
            if (isWord(c) || c.toString() == "_" || c.toString() == "-") {
                tmpWX.append(c)
            }
            //收集QQ和手机号的
            if (isNum(c)) {
                tmpNum.append(c)
            }
        }
        if (tmpWX.isEmpty()||tmpNum.isEmpty()){
            return strBuilder.toString()
        }
        if (isEnglish(tmpWX.first().toString())) {//第一个字是英语
            if (tmpWX.length >= 6) {
                var tmp2 = StringBuilder()
                for (c in strBuilder.toString().toCharArray()) {
                    if (isWord(c) || c.toString() == "_" || c.toString() == "-") {
                        tmp2.append("*")
                        continue
                    }
                    tmp2.append(c)
                }
                return tmp2.toString()//微信
            }
        }
        //QQ或者手机号
        if (tmpNum.toString().matches("[1-9]\\d{7,12}".toRegex()) || tmpNum.toString().matches("/^[1]([3-9])[0-9]{9}\$/".toRegex())) {
            var tmp2 = StringBuilder()
            for (c in strBuilder.toString().toCharArray()) {
                if (isNum(c)) {
                    tmp2.append("*")
                    continue
                }
                tmp2.append(c)
            }
            return tmp2.toString()
        }

        return strBuilder.toString()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        var f1 = filterLiveMsg("3751阿萨斯56483")
        f1 = filterLiveMsg("撒的撒微信aaa123")
        f1 = filterLiveMsg("WEIxin158")
        f1 = filterLiveMsg("18767阿萨斯164436")
        f1 = filterLiveMsg("1234567阿萨斯")
        f1 = filterLiveMsg("12阿萨斯345678")
        f1 = filterLiveMsg("12345678阿萨斯9012")
        f1 = filterLiveMsg("1234阿萨斯567890123")
    }

    /**
     * 校验一个strinng是否是汉字
     * @return true代表是汉字
     */
    @JvmStatic
    fun isChineseChar(c: String): Boolean {
        try {
            return c.toByteArray(charset("UTF-8")).size > 1
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /**
     * 校验一个字符是否是汉字
     * @return true代表是汉字
     */
    @JvmStatic
    fun isChineseChar(c: Char): Boolean {
        try {
            return c.toString().toByteArray(charset("UTF-8")).size > 1
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    /**
     * 校验某个字符是否是a-z、A-Z、_、0-9
     *
     * @param c
     * 被校验的字符
     * @return true代表符合条件
     */
    @JvmStatic
    fun isWord(c: Char): Boolean {
        val regEx = "[\\w]"
        val p = Pattern.compile(regEx)
        val m = p.matcher("" + c)
        return m.matches()
    }

    /**
     *
     * 是否是英文
     */
    fun isEnglish(charaString: String): Boolean {
        return charaString.matches("^[a-zA-Z]*".toRegex())
    }

    /**
     *
     * 是否是数字
     */
    fun isNum(c: Char): Boolean {
        return c.toString().matches("^[0-9]*".toRegex())
    }
}