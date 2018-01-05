package cn.tonghao.remex.common.method;

import org.springframework.core.MethodParameter;

/**
 * Created by howetong on 2018/1/5.
 */
public interface BeanWrapper {
    boolean supportsType(MethodParameter var1);

    Object wrap(Object var1);
}
