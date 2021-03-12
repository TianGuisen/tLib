package a.tlib.utils

import java.text.SimpleDateFormat
import java.util.*


/**
 * <h3>日期工具类</h3>
 *
 * 主要实现了日期的常用操作
 */
object DateUtil {

    /**
     * yyyy-MM-dd HH:mm:ss字符串
     */
    val DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"

    /**
     * 获取当前日期
     */
    fun getCurrentTime(formatterStr: String): String {
        return SimpleDateFormat(formatterStr).format(Date(System.currentTimeMillis()))
    }

    /**
     * 将date转成字符串
     *
     * @param date   Date
     * @param format SimpleDateFormat
     * <br></br>
     * 注： SimpleDateFormat为空时，采用默认的yyyy-MM-dd HH:mm:ss格式
     * @return yyyy-MM-dd HH:mm:ss
     */
    fun dateSimpleFormat(date: Date?, format: SimpleDateFormat?): String {
        var format2 = format
        if (format == null)
            format2 = defaultDateTimeFormat.get()
        return if (date == null) "" else format2!!.format(date)
    }

    /**
     * 将时间戳date 按照formatStr格式转成字符串
     * @param format 格式比如yyyy-MM-dd HH:mm:ss
     * @return yyyy-MM-dd HH:mm:ss
     */
    fun dateSimpleFormat(date: Date, formatStr: String): String {
        return SimpleDateFormat(formatStr).format(date)
    }

    /**
     * 将时间戳date long按照formatStr格式转成字符串
     * @param format 格式比如yyyy-MM-dd HH:mm:ss
     * @return yyyy-MM-dd HH:mm:ss
     */
    fun dateSimpleFormat(date: Long, formatStr: String): String {
        var date = date
        if (date < 10000000000) {
            date = date * 1000
        }
        return SimpleDateFormat(formatStr).format(date)
    }

    /**
     * 秒转时间
     * second时间
     * 后面为对应时间的单位文字，比如天/时/分，比如-，比如/
     */
    fun secondToTime(second: Long, dayUnitStr: String? = null, hourUnitStr: String? = null, minuteUnitStr: String? = null, secondUnitStr: String = ""): String {
        var str = ""
        var second = second
        val day = second / (24 * 60 * 60)
        if (day > 0 && dayUnitStr != null) {
            str = day.toString() + dayUnitStr
        }
        second = second - day * 24 * 60 * 60
        val hour = second / (60 * 60)
        if (hour > 0 && hourUnitStr != null) {
            str = str + hour.toString() + hourUnitStr
        }
        second = second - hour * 60 * 60
        val minute = second / 60
        if (minute > 0 && minuteUnitStr != null) {
            str = str + minute.toString() + minuteUnitStr
        }
        second = second - minute * 60
        if (second > 0) {
            str = str + second.toString() + secondUnitStr
        }
        return str
    }

    /**
     * yyyy-MM-dd HH:mm:ss格式
     */
    val defaultDateTimeFormat: ThreadLocal<SimpleDateFormat> = object : ThreadLocal<SimpleDateFormat>() {

        override fun initialValue(): SimpleDateFormat {
            return SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT)
        }

    }

    /**
     * 将long时间转成yyyy-MM-dd HH:mm:ss字符串<br></br>
     *
     * @param timeInMillis 时间long值
     * @return yyyy-MM-dd HH:mm:ss
     */
    fun getDateTimeFromMillis(timeInMillis: Long): String {
        return dateSimpleFormat(timeInMillis, "yyyy-MM-dd HH:mm:ss")
    }

    /**
     * 获得mm:ss的时间
     *
     * @param date
     * @return
     */
    @JvmStatic
    fun getTimeMSFormat(date: Date): String {
        return dateSimpleFormat(date, "mm:ss")
    }


    fun parseLiveFinishTime(time: Long): String {
        var timestr = ""
        var second = time % 60
        var min = (time % (60 * 60)) / 60
        var hour = (time % (60 * 60 * 24)) / (60 * 60)
        if (hour >= 10) {
            timestr += "${hour}:"
        } else {
            timestr += "0${hour}:"
        }
        if (min >= 10) {
            timestr += "${min}:"
        } else {
            timestr += "0${min}:"
        }
        if (second >= 10) {
            timestr += "${second}"
        } else {
            timestr += "0${second}"
        }
        return timestr
    }

    fun pareLivetime(dateStr: String): Long {
        var yearstr = Calendar.getInstance().get(Calendar.YEAR).toString()
        var date = SimpleDateFormat("yyyy年MM月dd日 HH时mm分").parse(yearstr + "年" + dateStr)
        if ((date.time + 864000000) < System.currentTimeMillis()) {  // 翻年的算法处理
            date = SimpleDateFormat("yyyy年MM月dd日 HH时mm分").parse((Calendar.getInstance().get(Calendar.YEAR) + 1).toString() + "年" + dateStr)
        }
        return date.time / 1000
    }


    /**
     * 根据制定的单位获取特定的日期
     */
    fun getUnitTime(time: Long, unit: String): Int {
        var value = 0
        var second = time / 1000
        //天
        var day = second / (3600 * 24)
        second = second - (day * 3600 * 24)
        //小时
        var hour = second / 3600
        second = second - hour * 3600
        //分钟
        var minute = second / 60
        //秒
        second = second - minute * 60
        when (unit) {
            "DAY" -> {
                value = day.toInt()
            }
            "HOUR" -> {
                value = hour.toInt()
            }
            "MINUTE" -> {
                value = minute.toInt()
            }
            "SECOND" -> {
                value = second.toInt()
            }
        }
        return value
    }
}
