package a.tlib.utils.gson

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.LinkedHashSet


/**
 * 不直接对外暴露gson对象，只提供方法
 */
object GsonUtil {
    /**
     * 防止后端传null字段，统一设置默认值
     */
    @JvmField
    val gson = GsonBuilder()
            .registerTypeAdapter(Int::class.java, IntTypeAdapter())
            .registerTypeAdapter(Double::class.java, DoubleTypeAdapter())
            .registerTypeAdapter(Long::class.java, LongTypeAdapter())
            .registerTypeAdapter(String::class.java, StringTypeAdapter())
            .registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
            //当某个字段我们声明为List<T>，而后台返回T或其他异常类型时，会自动转为List<T>
            .registerTypeHierarchyAdapter(List::class.java, JsonDeserializer { json, typeOfT, context ->
                try {
                    if (json.isJsonObject() || json.isJsonPrimitive()) {
                        val list = ArrayList<Any>()
                        val itemType: Type = (typeOfT as ParameterizedType).getActualTypeArguments().get(0)
                        list.add(context.deserialize(json, itemType))
                        list
                    } else if (json.isJsonArray) {
                        val array = json.asJsonArray
                        val itemType: Type = (typeOfT as ParameterizedType).getActualTypeArguments().get(0)
                        val list = ArrayList<Any>()
                        for (i in 0 until array.size()) {
                            val element = array[i]
                            list.add(context.deserialize(element, itemType))
                        }
                        list
                    } else {
                        //和接口类型不符，返回空List
                        Collections.EMPTY_LIST
                    }
                } catch (e: Exception) {
                    Collections.EMPTY_LIST
                }
            })
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
     * 将jsonString转成泛型bean
     */
    @JvmStatic
    fun <T> toBean(jsonString: String, type: java.lang.reflect.Type): T = gson.fromJson(jsonString, type)

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

    /**
     * 转成Set
     */
    @JvmStatic
    fun <T> toSet(jsonString: String): MutableSet<T> = gson.fromJson(jsonString, object : TypeToken<MutableSet<T>>() {}.type)
    /**
     * 转成LinkdHashSet
     */
    @JvmStatic
    fun <T> toLinkdHashSet(jsonString: String): LinkedHashSet<T> = gson.fromJson(jsonString, object : TypeToken<LinkedHashSet<T>>() {}.type)

    /**
     * 相同字段名，但是类型可能是T和List<T>甚至null，定义字段名为Any
     * 输入T转为List<T>
     *
     * @param filedObjValue 需要处理的字段对象值
     * @param filedMapClass 处理的字段对象映射Class
     * @return
     */
    @JvmStatic
    fun <T> transList(filedObjValue: Any? = null, filedMapClass: Class<T>): List<T> {
        val jsonStr: String = gson.toJson(filedObjValue)
        val parser = JsonParser()
        val element: JsonElement = parser.parse(jsonStr)
        if (element.isJsonObject() || element.isJsonPrimitive()) {
            val beanList: MutableList<T> = ArrayList()
            val t = toBean(jsonStr, filedMapClass)
            beanList.add(t)
            return beanList
        } else if (element.isJsonArray()) {
            val list: List<T> = toList(jsonStr, filedMapClass)
            return list
        } else {
            return mutableListOf()
        }
    }
}