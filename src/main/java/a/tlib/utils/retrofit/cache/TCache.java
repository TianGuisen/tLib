package a.tlib.utils.retrofit.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;

/**
 * @author 田桂森 2021/7/3 0003
 * 缓存注解，单位秒
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, PARAMETER})
public @interface TCache {
    /**
     * 无网络使用缓存
     */
    public static final int MODE_NET = 0;
    
    /**
     * 一直使用缓存
     */
    public static final int MODE_ALWAYS = 1;
    
    /**
     * 缓存时间，秒
     *
     * @return
     */
    int value() default 0;
    
    boolean enable() default true;
    
    int mode() default MODE_ALWAYS;
}