package cn.tonghao.component;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by tonghao on 2017/6/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/spring-web.xml",
        "classpath:config/spring/spring-redis.xml",
        "classpath:config/spring/spring-dal.xml"})
public class StringRedisTempTest {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void boundHashOpts() {
        String key = "REMEX_CONFIG_VALUE_user.name";
        BoundValueOperations<String, String> boundValueOperations = stringRedisTemplate.boundValueOps(key);
        System.out.println(boundValueOperations.get());
        //boundValueOperations.expireAt(new Date());
        stringRedisTemplate.expire(key,0, TimeUnit.MILLISECONDS);
//        boundValueOperations.setIfAbsent("testValue");
        //System.out.println(boundValueOperations.getExpire());
        System.out.println(stringRedisTemplate.getExpire(key));
    }
}

