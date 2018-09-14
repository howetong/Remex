package cn.tonghao.remex.business.pay.strategy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * 策略工厂
 * Created by howetong on 2018/9/11.
 */
@Component
public class ChannelStrategyFactory implements BeanFactoryAware {

    private static BeanFactory beanFactory;

    @SuppressWarnings("AccessStaticViaInstance")
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public static BeanFactory getBeanFactory() {
        return beanFactory;
    }
}
