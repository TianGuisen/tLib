package a.tlib.utils.encrypt

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * @version:      由有播科技（杭州）有限公司开发
 * @date:         2019/1/24  15:53 , @author:  qaufu
 */
object MD5Util {
    /**
     * 时间戳
     */
    var TimeDifference: Long = 0
    val appKay = "Android_1.0"
    val appSecret = "9edd61b7c87b3a556def3e4ce073caf6"
    var infoStr = ""
    fun setMD5(stringMap: HashMap<String, String>): HashMap<String, String> {
        stringMap["app_key"] = appKay
        stringMap["_time"] = (System.currentTimeMillis() / 1000 - TimeDifference).toString() + ""
        infoStr = ""
        for ((k, v) in MapSort(stringMap)) {
            infoStr += "${k}${v}"
        }
        infoStr = infoStr.replace("\\", "\\\\").replace("\"", "\\\"")
        infoStr = appKay + infoStr + appSecret
        stringMap["_sign"] = encode((encode(infoStr)))
        return stringMap
    }

    private fun MapSort(hashMap: HashMap<String, String>): Map<String, String> {
        val sortMap = TreeMap<String, String>(
                Comparator { o1, o2 -> o1.compareTo(o2) })
        sortMap.putAll(hashMap)
        return sortMap
    }

    fun encode(text: String): String {
        try {
            //获取md5加密对象
            val instance: MessageDigest = MessageDigest.getInstance("MD5")
            //对字符串加密，返回字节数组
            val digest: ByteArray = instance.digest(text.toByteArray())
            var sb: StringBuffer = StringBuffer()
            for (b in digest) {
                //获取低八位有效值
                var i: Int = b.toInt() and 0xff
                //将整数转化为16进制
                var hexString = Integer.toHexString(i)
                if (hexString.length < 2) {
                    //如果是一位的话，补0
                    hexString = "0" + hexString
                }
                sb.append(hexString)
            }
            return sb.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return ""
    }
}