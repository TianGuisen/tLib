package a.tlib.utils

import java.lang.reflect.InvocationTargetException

/**
 * @author 田桂森 2021/5/12 0012
 * 反射工具类
 */
object ReflectUtil {
    /**
     * 得到某个对象的公共属性
     *
     * @param owner, fieldName
     * @return 该属性对象
     * @throws Exception
     */
    @Throws(Exception::class)
    @JvmStatic
    fun getProperty(owner: Any, fieldName: String): Any {
        val ownerClass: Class<*> = owner.javaClass
        val field = ownerClass.getDeclaredField(fieldName)
        return field[owner]
    }

    /**
     * 得到某类的静态公共属性
     *
     * @param className 类名
     * @param fieldName 属性名
     * @return 该属性对象
     * @throws Exception
     */
    @JvmStatic
    @Throws(Exception::class)
    fun getStaticProperty(className: String, fieldName: String): Any {
        val ownerClass = Class.forName(className)
        val field = ownerClass.getDeclaredField(fieldName)
        return field[ownerClass]
    }

    /**
     * 执行某对象方法
     *
     * @param owner      对象
     * @param methodName 方法名
     * @param args       参数
     * @return 方法返回值
     * @throws Exception
     */
    @JvmStatic
    @Throws(Exception::class)
    fun invokeMethod(owner: Any, methodName: String, args: Array<Any>): Any {
        val ownerClass: Class<*> = owner.javaClass
        val argsClass: Array<Class<*>?> = arrayOfNulls(args.size)
        var i = 0
        val j = args.size
        while (i < j) {
            argsClass[i] = args[i].javaClass
            i++
        }
        val method = ownerClass.getMethod(methodName, *argsClass)
        return method.invoke(owner, *args)
    }

    /**
     * 执行某类的静态方法
     *
     * @param className  类名
     * @param methodName 方法名
     * @param args       参数数组
     * @return 执行方法返回的结果
     * @throws Exception
     */
    @JvmStatic
    @Throws(Exception::class)
    fun invokeStaticMethod(className: String, methodName: String,
                           args: Array<Any>): Any {
        val ownerClass = Class.forName(className)
        val argsClass: Array<Class<*>?> = arrayOfNulls(args.size)
        var i = 0
        val j = args.size
        while (i < j) {
            argsClass[i] = args[i].javaClass
            i++
        }
        val method = ownerClass.getMethod(methodName, *argsClass)
        return method.invoke(null, *args)
    }

    /**
     * 新建实例
     *
     * @param className 类名
     * @param args      构造函数的参数
     * 如果无构造参数，args 填写为 null
     * @return 新建的实例
     * @throws Exception
     */
    @JvmStatic
    @Throws(NoSuchMethodException::class, SecurityException::class, ClassNotFoundException::class, InstantiationException::class, IllegalAccessException::class, IllegalArgumentException::class, InvocationTargetException::class)
    fun newInstance(className: String, args: Array<Any?>?, argsType: Array<Class<*>?>): Any {
        val newoneClass = Class.forName(className)
        return if (args == null) {
            newoneClass.newInstance()
        } else {
            //             Class[] argsClass = new Class[args.length];
            //
            //             for (int i = 0, j = args.length; i < j; i++) {
            //                 argsClass[i] = args[i].getClass();
            //             }
            //
            //             Constructor cons = newoneClass.getConstructor(argsClass);
            val cons = newoneClass.getConstructor(*argsType)
            cons.newInstance(*args)
        }
    }

    /**
     * 是不是某个类的实例
     *
     * @param obj 实例
     * @param cls 类
     * @return 如果 obj 是此类的实例，则返回 true
     */
    @JvmStatic
    fun isInstance(obj: Any?, cls: Class<*>): Boolean {
        return cls.isInstance(obj)
    }

    /**
     * 得到数组中的某个元素
     *
     * @param array 数组
     * @param index 索引
     * @return 返回指定数组对象中索引组件的值
     */
    @JvmStatic
    fun getByArray(array: Any?, index: Int): Any {
        return java.lang.reflect.Array.get(array, index)
    }

    @JvmStatic
    fun GetClassListByPackage(pPackage: String?): Class<*> {
        val _Package = Package.getPackage(pPackage)
        return _Package.javaClass
    }
}