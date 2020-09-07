package a.tlib.utils.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * @author 田桂森 2020/2/28
 */
class BooleanTypeAdapter : TypeAdapter<Boolean?>() {
    override fun write(out: JsonWriter, value: Boolean?) {
        var value = value
        try {
            if (value == null) {
                value = false
            }
            out.value(value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun read(json: JsonReader): Boolean? {
        try {
            val value: Boolean
            if (json.peek() == JsonToken.NULL) {
                json.nextNull()
                return false
            }
            if (json.peek() == JsonToken.NUMBER) {
                val b = json.nextInt()
                return b==1
            }
            return if (json.peek() == JsonToken.STRING) {
                val str = json.nextString()
                try {
                    str.toBoolean()
                } catch (e: NumberFormatException) {
                  false
                }
            } else {
                value = json.nextBoolean()
                value
            }
        } catch (e: Exception) {
        }
        return false
    }
}