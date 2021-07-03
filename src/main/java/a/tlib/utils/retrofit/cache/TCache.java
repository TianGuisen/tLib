package a.tlib.utils.retrofit.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 田桂森 2021/7/3 0003
 * 缓存注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TCache {
    /**
     * 缓存时间，秒
     * @return
     */
    int value() default 0;
    boolean enable() default true;
}