package cn.tonghao.component.hystrixlearn;

import cn.tonghao.remex.business.test.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

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

    @Resource
    private TestService testService;

    @Test
    public void invoke() throws Exception{
        for (int i = 0; i < 10; i++) {
            String result = testService.getInfo();
            System.out.println(result);
        }
    }

    }
