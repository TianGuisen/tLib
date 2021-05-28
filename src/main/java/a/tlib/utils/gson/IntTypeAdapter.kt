package a.tlib.utils.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * @author 田桂森 2020/2/28
 * null-0
 * true-1
 * false-0
 * string.toInt
 * exception-0
 */
class IntTypeAdapter : TypeAdapter<Int?>() {
    override fun write(out: JsonWriter, value: Int?) {
        var value = value
        try {
            if (value == null) {
                value = 0
            }
            out.value(value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun read(json: JsonReader): Int? {
        try {
            val value: Int
            if (json.peek() == JsonToken.NULL) {
                json.nextNull()
                return 0
            }
            if (json.peek() == JsonToken.BOOLEAN) {
                val b = json.nextBoolean()
                return if (b) 1 else 0
            }
            return if (json.peek() == JsonToken.STRING) {
                val str = json.nextString()
                try {
                    str.toInt()
                } catch (e: NumberFormatException) {
                    0
                }
            } else {
                value = json.nextInt()
                value
            }
        } catch (e: Exception) {
        }
        return 0
    }
}