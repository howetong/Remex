package cn.tonghao.remex.business.test.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

/**
 * 测试用
 * Created by howetong on 2018/9/28.
 */
@Service
public class TestServiceImpl implements ITestService {

    @HystrixCommand(groupKey = "AnnotationHystrixInvoke", commandKey = "getInfo", fallbackMethod = "getFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")
            }
    )

    @Override
    public String getInfo() {
        System.out.println(Thread.currentThread().getName() + "开始执行getInfo");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("捕获到异常" + e.getMessage());
        }
        System.out.println(Thread.currentThread().getName() + "仍在执行getInfo");
        return "hello";
    }

    public String getFallback() {
        System.out.println(Thread.currentThread().getName() + "开始执行getFallback");
        return "bye bye";
    }
}
