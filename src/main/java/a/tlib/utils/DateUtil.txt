package a.tlib.utils

import android.util.ArrayMap
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


/**
 * <h3>日期工具类</h3>
 *
 * 主要实现了日期的常用操作
 */
object DateUtil {

    /**
     * yyyy-MM-dd HH:mm:ss字符串
     */
    @JvmStatic
    val DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"

    /**
     * 最后一次调用接口获取的服务器时间戳，秒
     */
    @JvmStatic
    var lastServerTime = 0L
        set(value) {
            field = value
            timeDiff = field - getCurrentSecond()
        }

    /**
     * 手机时间和服务器时间的差额，正数表示慢了
     * 使用DateUtil.getCurrentSecond()获取时间后加上这个时间进行补正
     */
    @JvmStatic
    var timeDiff = 0L

    /**
     * 获取系统当前时间戳秒数
     */
    @JvmStatic
    fun getCurrentSecond(): Long {
        return getCurrentMillisecond() / 1000
    }

    /**
     * 获取服务器当前时间戳秒数
     */
    @JvmStatic
    fun getCurrentServerSecond(): Long {
        return getCurrentMillisecond() / 1000 + timeDiff
    }

    /**
     * 获取系统当前时间戳毫秒数
     */
    @JvmStatic
    fun getCurrentMillisecond(): Long {
        return System.currentTimeMillis()
    }

    /**
     * long时间转Calendar
     */
    @JvmStatic
    fun millisToCalendar(time: Long): Calendar {
        var date = Calendar.getInstance()
        date.timeInMillis = time
        return date
    }

    /**
     * 日期格式转化
     * 1转换成01
     */
    @JvmStatic
    fun parseDate2Bit(date: Int): String {
        return if (date < 10) "0$date" else date.toString()
    }

    /**
     * 获取某年某月有多少天
     */
    @JvmStatic
    fun getDayOfMontn(year: Int, month: Int): Int {
        var calendar = Calendar.getInstance()
        calendar.set(year, month, 0)
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    /**
     * 当前年份
     */
    @JvmStatic
    fun getCurrentYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    /**
     * 当前月
     */
    @JvmStatic
    fun getCurrentMonth(): Int {
        return Calendar.getInstance().get(Calendar.MONTH) + 1
    }

    /**
     * 当前天
     */
    @JvmStatic
    fun getCurrentDay(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    }

    /**
     * 当前小时
     */
    @JvmStatic
    fun getCurrentHour(): Int {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    }

    /**
     * 当前分钟
     */
    @JvmStatic
    fun getCurrentMinute(): Int {
        return Calendar.getInstance().get(Calendar.MINUTE)
    }


    /**
     * 获取当前日期
     */
    @JvmStatic
    fun getCurrentTime(formatterStr: String = DEFAULT_DATE_TIME_FORMAT): String {
        return SimpleDateFormat(formatterStr).format(Date(System.currentTimeMillis()))
    }

    /**
     * 将时间戳date 按照formatStr格式转成字符串
     * @param format 格式比如yyyy-MM-dd HH:mm:ss
     * @return yyyy-MM-dd HH:mm:ss
     */
    @JvmStatic
    fun dateSimpleFormat(date: Date, formatStr: String = DEFAULT_DATE_TIME_FORMAT): String {
        return SimpleDateFormat(formatStr).format(date)
    }

    /**
     * 将时间戳date long按照formatStr格式转成字符串
     * @param format 格式比如yyyy-MM-dd HH:mm:ss
     * @return yyyy-MM-dd HH:mm:ss
     */
    @JvmStatic
    fun dateSimpleFormat(date: Long, formatStr: String = DEFAULT_DATE_TIME_FORMAT): String {
        var date = date
        if (date < 10000000000) {
            date = date * 1000
        }
        return SimpleDateFormat(formatStr).format(date)
    }

    /**
     * 秒数转map
     * day,hour,minute,second
     * 0,1,2,3
     */
    @JvmStatic
    fun secondToTimeMap(second: Long): HashMap<String, Long> {
        val map = hashMapOf<String, Long>()
        var second = second
        val day = second / (24 * 60 * 60)
        map.put("day", day)
        map.put("0", day)
        second = second - day * 24 * 60 * 60
        val hour = second / (60 * 60)
        map.put("hour", hour)
        map.put("1", hour)
        second = second - hour * 60 * 60
        val minute = second / 60
        map.put("minute", minute)
        map.put("2", minute)
        second = second - minute * 60
        map.put("second", second)
        map.put("3", second)
        return map
    }

    /**
     * 时间字符串转为Long时间戳，毫秒
     */
    @JvmStatic
    fun dateStr2Long(dateStr: String, formatStr: String = DEFAULT_DATE_TIME_FORMAT): Long {
        //Date或者String转化为时间戳
        val format = SimpleDateFormat(formatStr)
        val date = format.parse(dateStr)
        return date.time
    }
}
