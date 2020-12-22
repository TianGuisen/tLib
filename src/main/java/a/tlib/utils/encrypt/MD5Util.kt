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
    @JvmField
    var TimeDifference: Long = 0

    @JvmField
    val appKay = "Android_1.0"

    @JvmField
    val appSecret = "9edd61b7c87b3a556def3e4ce073caf6"

    @JvmStatic
    fun setMD5(stringMap: Hashtable<String, String>): Hashtable<String, String> {
        stringMap.put("app_key", appKay)
        stringMap.put("_time", (System.currentTimeMillis() / 1000 - TimeDifference).toString())
        var infoStr = ""
        for ((k, v) in MapSort(stringMap)) {
            infoStr += "${k}${v}"
        }
//        val mapDescSort = stringMap.entries.sortedByDescending  { it.value }
//                .associateBy({ it.key }, { it.value })
//        mapDescSort.forEach {
//            infoStr=infoStr+(it.key + it.value)
//        }
//        infoStr = infoStr.replace("\\", "\\\\").replace("\"", "\\\"")
        infoStr = appKay + infoStr + appSecret
        stringMap.put("_sign", encode((encode(infoStr))))
        return stringMap
    }

    @JvmStatic
    private fun MapSort(hashMap: Hashtable<String, String>): Map<String, String> {
        val sortMap = TreeMap<String, String>(
                Comparator { o1, o2 -> o1.compareTo(o2) })
        sortMap.putAll(hashMap)
        return sortMap
    }

    @JvmStatic
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