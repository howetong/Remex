package cn.tonghao.remex.common.method.impl;

import cn.tonghao.remex.common.method.AbstractBeanWrapper;
import cn.tonghao.remex.common.method.SuccessData;
import org.springframework.core.MethodParameter;

/**
 * Created by howetong on 2018/1/5.
 */
public class DefaultBeanWrapper extends AbstractBeanWrapper {
    public DefaultBeanWrapper() {
    }

    public Object wrap(Object bean) {
        return new SuccessData(bean);
    }

    public boolean supports(MethodParameter returnType) {
        return true;
    }
}