package cn.tonghao.remex.common.method.impl;

import cn.tonghao.remex.common.method.AbstractBeanWrapper;
import cn.tonghao.remex.common.util.Response;
import org.springframework.core.MethodParameter;

/**
 * Created by howetong on 2018/1/5.
 */
public class ResponseWrapper extends AbstractBeanWrapper {
    public ResponseWrapper() {
    }

    public Object wrap(Object bean) {
        return bean;
    }

    public boolean supports(MethodParameter returnType) {
        return Response.class.isAssignableFrom(returnType.getParameterType());
    }
}
