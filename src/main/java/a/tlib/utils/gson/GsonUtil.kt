package a.tlib.utils.gson

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken


/**
 * 不直接对外暴露gson对象，只提供方法
 */
object GsonUtil {
    //防止后端传null字段，统一设置默认值
    @JvmField
    val gson = GsonBuilder()
            .registerTypeAdapter(Int::class.java, IntTypeAdapter())
            .registerTypeAdapter(Double::class.java, DoubleTypeAdapter())
            .registerTypeAdapter(Long::class.java, LongTypeAdapter())
            .registerTypeAdapter(String::class.java, StringTypeAdapter())
            .registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
            .create()

    /**
     * 转为jsonString
     */
    @JvmStatic
    fun toJson(any: Any): String = gson.toJson(any)

    /**
     * 将jsonString转成泛型bean
     */
    @JvmStatic
    fun <T> toBean(jsonString: String, cls: Class<T>): T = gson.fromJson(jsonString, cls)

    /**
     * 转成list
     * 解决泛型在编译期类型被擦除导致报错
     */
    @JvmStatic
    fun <T> toList(json: String, cls: Class<T>): List<T> {
        val list = ArrayList<T>()
        val array = JsonParser().parse(json).getAsJsonArray()
        for (elem in array) {
            list.add(gson.fromJson<T>(elem, cls))
        }
        return list
    }
    /**
     * 转成list
     * 解决泛型在编译期类型被擦除导致报错
     */
    @JvmStatic
    fun <T> toMutableList(json: String, cls: Class<T>): MutableList<T> {
        val list = mutableListOf<T>()
        val array = JsonParser().parse(json).getAsJsonArray()
        for (elem in array) {
            list.add(gson.fromJson<T>(elem, cls))
        }
        return list
    }
    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    @JvmStatic
    fun <T> GsonToListMaps(gsonString: String): List<MutableMap<String, T>> = gson.fromJson(gsonString, object : TypeToken<List<MutableMap<String, T>>>() {}.type)

    /**
     * 转成map
     */
    @JvmStatic
    fun <T> toMap(jsonString: String): MutableMap<String, T> = gson.fromJson(jsonString, object : TypeToken<MutableMap<String, T>>() {}.type)


}