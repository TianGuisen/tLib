package a.tlib.utils.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * @author 田桂森 2020/2/28
 * null-0.0
 * boolean-0.0
 * string.toDouble
 * exception-0.0
 */
class DoubleTypeAdapter : TypeAdapter<Double?>() {
    override fun write(out: JsonWriter, value: Double?) {
        var value = value
        try {
            if (value == null) {
                value = 0.0
            }
            out.value(value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun read(json: JsonReader): Double? {
        try {
            if (json.peek() == JsonToken.NULL) {
                json.nextNull()
                return 0.0
            }
            if (json.peek() == JsonToken.BOOLEAN) {
                val b = json.nextBoolean()
                return 0.0
            }
            return if (json.peek() == JsonToken.STRING) {
                val str = json.nextString()
                try {
                    str.toDouble()
                } catch (e: NumberFormatException) {
                    0.0
                }
            } else {
                val value = json.nextDouble()
                value ?: 0.0
            }
        } catch (e: Exception) {
        }
        return 0.0
    }
}