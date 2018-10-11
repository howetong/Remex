package cn.tonghao.remex.business.hystrixlearn;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rx.Observable;
import rx.Observer;

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
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("HelloWorld")));
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
           HystrixCommand<String> hystrixCommand = new CommandHelloWorld("world");
           String result = hystrixCommand.execute();
           System.out.println(result);
        }

        @Test
        // 异步调用
        public void testAsynchronous() throws Exception {
            HystrixCommand<String> hystrixCommand = new CommandHelloWorld("world");
            Future<String> future = hystrixCommand.queue();
            String result = future.get();
            System.out.println(result);
        }

        @Test
        // 反应执行
        public void testObservable() throws Exception {
            Observable<String> observable = new CommandHelloWorld("world").observe();
            // 同步阻塞
            String blockingResult = observable.toBlocking().single();
            System.out.println(blockingResult);

            // 非阻塞
            observable.subscribe(new Observer<String>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(String s) {
                    System.out.println("onNext: " + s);
                }
            });
        }
    }
}

