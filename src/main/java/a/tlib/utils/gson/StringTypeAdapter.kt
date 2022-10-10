package a.tlib.utils.gson

import android.text.TextUtils
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * @author 田桂森 2020/2/28
 */
class StringTypeAdapter : TypeAdapter<String?>() {
    override fun write(out: JsonWriter, value: String?) {
        var value = value
        try {
            if (TextUtils.isEmpty(value)) {
                value = ""
            }
            out.value(value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun read(json: JsonReader): String? {
        try {
            if (JsonToken.NULL == json.peek()) {
                json.nextNull()
                return ""
            }
            if (json.peek() == JsonToken.BOOLEAN) {
                val b = json.nextBoolean()
                //                return b ? "true" : "false";
                return ""
            }
            return json.nextString()
        } catch (e: Exception) {
        }
        return ""
    }
}