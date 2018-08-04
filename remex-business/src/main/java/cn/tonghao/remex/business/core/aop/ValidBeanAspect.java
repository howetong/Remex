package cn.tonghao.remex.business.core.aop;

import cn.tonghao.remex.common.exception.BusinessException;
import cn.tonghao.remex.common.exception.ErrorCodeDefinition;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by howetong on 2018/1/5.
 */
@Component
@Aspect
public class ValidBeanAspect {

    @Before("@annotation(cn.tonghao.remex.business.core.annotation.BeanValid)")
    public void checkBeanValid(JoinPoint joinPoint) throws Exception{
        List<Object> args = Arrays.asList(joinPoint.getArgs());
        try {
            args.stream().forEach(DataValidationUtil::validate);
        } catch (RuntimeException e) {
            throw new BusinessException(ErrorCodeDefinition.INVALID_PARAMETER, "参数不合法," + e.getMessage());
        }
    }
}
