package cn.tonghao.remex.business.hystrixlearn;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.Future;

/**
 * hystrix hello world demo
 * see https://github.com/Netflix/Hystrix/wiki/How-To-Use#Hello-World
 * Created by howetong on 2018/9/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class CommandHelloWorld extends HystrixCommand<String> {

    private final String name;


    public CommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        return "Hello " + name + "!";
    }

    public static class UnitTest {

        @Test
        // 同步调用
        public void testSynchronous() throws Exception {
           HystrixCommand<String> hystrixCommand1 = new CommandHelloWorld("world");
           HystrixCommand<String> hystrixCommand2 = new CommandHelloWorld("Bob");
           String result1 = hystrixCommand1.execute(); // execute内部调用了queue().get()
           String result2 = hystrixCommand2.queue().get();
           System.out.println(result1);
           System.out.println(result2);
        }

        @Test
        // 异步调用
        public void testAsynchronous1() throws Exception {
            Future<String> fWorld = new CommandHelloWorld("world").queue();
            String result = fWorld.get();
            System.out.println(result);
        }
    }
}
