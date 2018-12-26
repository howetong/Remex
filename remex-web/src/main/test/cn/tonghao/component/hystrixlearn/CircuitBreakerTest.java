package cn.tonghao.component.hystrixlearn;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 熔断测试
 * Created by howetong on 2018/9/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:config/spring/spring-web.xml",
})
public class CircuitBreakerTest {

    public static int num = 0;
    static HystrixCommand.Setter setter = HystrixCommand.Setter
            .withGroupKey(HystrixCommandGroupKey.Factory.asKey("circuitBreakerTestGroup"))
            .andCommandKey(HystrixCommandKey.Factory.asKey("circuitBreakerTestCommand"))
            .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("circuitBreakerTestPool"))
            .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(10)) // 配置线程池
            .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                                            .withCircuitBreakerEnabled(true)
                                            .withCircuitBreakerRequestVolumeThreshold(10)
                                            .withCircuitBreakerErrorThresholdPercentage(50));
            // 未配置的值均取系统默认值
    HystrixCommand<Object> hystrixCommand = new HystrixCommand<Object>(setter) {

        @Override
        protected Object run() throws Exception {
            if (num % 2 == 0) {
                return num + "";
            } else {
                int j = 0;
                while (true) {
                    j++; // 死循环模拟超时
                }
            }
        }

        @Override
        protected Object getFallback() {
            System.out.println(getExecutionException());
            return "CircuitBreaker fallback:" + num;
        }
    };

    public static class UnitTest {
        @Test
        public void testCircuitBreaker() throws Exception {
            for (int i = 0; i < 60; i++) {
                CircuitBreakerTest.num = i;
                CircuitBreakerTest circuitBreakerTest = new CircuitBreakerTest();
                String result = (String) circuitBreakerTest.hystrixCommand.execute();
                System.out.println(result);
            }
        }
    }
}
