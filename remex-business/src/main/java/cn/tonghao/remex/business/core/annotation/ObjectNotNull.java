package cn.tonghao.remex.business.core.annotation;

import java.lang.annotation.*;

/**
 * 参数校验验证注解
 *
 * @author fengxuekui
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ObjectNotNull {

    String message() default "参数不能为空";

}

