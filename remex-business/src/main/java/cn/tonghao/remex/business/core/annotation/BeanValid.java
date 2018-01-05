package cn.tonghao.remex.business.core.annotation;


import java.lang.annotation.*;

/**
 * Created by howetong on 2018/1/5.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BeanValid {
    /* 是否必须校验 */
    boolean required() default true;
}
