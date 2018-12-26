package cn.tonghao.component.hystrixlearn;

import cn.tonghao.remex.business.test.hystrix.ITestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * hystrix注解
 * Created by howetong on 2018/9/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:config/spring/spring-web.xml",
        "classpath:config/spring/spring-dal.xml",
        "classpath:config/spring/spring-redis.xml",
})
public class AnnotationHystrixInvoke {

    @Autowired
    private ITestService testService;

    @Test
    public void invoke() throws Exception{
        for (int i = 0; i < 10; i++) {
            String result = testService.getInfo();
            System.out.println(result);
        }
    }

}
