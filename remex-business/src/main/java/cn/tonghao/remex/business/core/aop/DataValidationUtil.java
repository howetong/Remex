package cn.tonghao.remex.business.core.aop;

import cn.tonghao.remex.business.core.log.RemexLogger;
import cn.tonghao.remex.common.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Created by howetong on 2017/11/5.
 */
public class DataValidationUtil {
    private static final Logger logger = RemexLogger.getLogger(DataValidationUtil.class);

    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    private static Validator validator = factory.getValidator();

    public static <T> void validate(T bean){
        StringBuffer sb = new StringBuffer();
        try {
            if (null == bean) {
                sb.append("校验的对象不能为null");
            } else {
                Set<ConstraintViolation<T>> constraintViolations = validator.validate(bean);
                for (ConstraintViolation<T> violation : constraintViolations) {
                    sb.append(violation.getPropertyPath()).append(":").append(violation.getMessage()).append(" ");
                }
            }
        } catch (RuntimeException e) {
            logger.error("数据校验发生异常,{}", e);
            sb.append("数据校验发生异常，请检查字段注解配置是否合法");
        }
        if(StringUtils.isNotEmpty(sb.toString())){
            logger.info("异常入参：{}", JsonUtil.toString(bean));
            throw new RuntimeException(sb.toString());
        }
    }
}
