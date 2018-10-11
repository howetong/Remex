package cn.tonghao.component.hystrixlearn;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * fallback demo
 * Created by howetong on 2018/9/26.
 */
public class CommandHelloFailure extends HystrixCommand<String>{

    private static final Logger logger = LoggerFactory.getLogger(CommandHelloFailure.class);

    private final String name;

    public CommandHelloFailure(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        logger.error("run thread#################" + Thread.currentThread().getName());
//        Thread.sleep(2000);
//        return "Hello world";
        throw new RuntimeException("this command always fails");
    }

    @Override
    protected String getFallback() {
        logger.error("getFallback thread#################" + Thread.currentThread().getName() + " commandKey:" + getCommandKey().name());
        return "Hello Failure " + name + "!";
    }


    public static class UnitTest {

        @Test
        public void testSynchronous() throws Exception {
            HystrixCommand<String> hystrixCommand = new CommandHelloFailure("world");
            String result = hystrixCommand.execute();
            System.out.println(result);
        }
    }
}
