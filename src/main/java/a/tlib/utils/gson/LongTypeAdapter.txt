package a.tlib.utils.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * @author 田桂森 2020/2/28
 * null-0l
 * true-1l
 * false-0l
 * string.toLong
 * exception-0l
 */
class LongTypeAdapter : TypeAdapter<Long?>() {
    override fun write(out: JsonWriter, value: Long?) {
        var value = value
        try {
            if (value == null) {
                value = 0L
            }
            out.value(value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun read(json: JsonReader): Long? {
        try {
            val value: Long
            if (json.peek() == JsonToken.NULL) {
                json.nextNull()
                return 0L
            }
            if (json.peek() == JsonToken.BOOLEAN) {
                val b = json.nextBoolean()
                return if (b) 1L else 0L
            }
            return if (json.peek() == JsonToken.STRING) {
                val str = json.nextString()
                try {
                    str.toLong()
                } catch (e: NumberFormatException) {
                    0L
                }
            } else {
                value = json.nextLong()
                value
            }
        } catch (e: Exception) {
        }
        return 0L
    }
}