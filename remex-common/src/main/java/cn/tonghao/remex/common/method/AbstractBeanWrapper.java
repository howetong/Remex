package cn.tonghao.remex.common.method;

import org.springframework.core.MethodParameter;

/**
 * Created by howetong on 2018/1/5.
 */
public abstract class AbstractBeanWrapper implements BeanWrapper {
    public AbstractBeanWrapper() {
    }

    public boolean supportsType(MethodParameter returnType) {
        return ResponseData.class.isAssignableFrom(returnType.getParameterType())?false:this.supports(returnType);
    }

    public abstract boolean supports(MethodParameter var1);
}
