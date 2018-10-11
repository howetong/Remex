package cn.tonghao.component.hystrixlearn;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

/**
 * hystrix hello world demo
 * Created by howetong on 2018/9/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ObservableCommandHelloWorld extends HystrixObservableCommand<String>{

    private final String name;

    public ObservableCommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected Observable<String> construct() {
        System.out.println("[construct] thread: " + Thread.currentThread().getName());
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> observer) {
                System.out.println("[construct-call] thread: " + Thread.currentThread().getName());
                if (!observer.isUnsubscribed()) {
                    observer.onNext("Hello1" + " thread: " +Thread.currentThread().getName());
                    observer.onNext("Hello2" + " thread: " +Thread.currentThread().getName());
                    observer.onNext(name + " thread:" +Thread.currentThread().getName());
                    System.out.println("complete before-----" + "thread: "  + Thread.currentThread().getName());
                    observer.onCompleted();
                    System.out.println("complete after------" + "thread: "  + Thread.currentThread().getName() + "\r\n");
                    observer.onCompleted(); // 不会执行到
                    observer.onNext("abc"); // 不会执行到
                }
            }
        });
    }

    public static class UnitTest {
        @Test
        // 反应执行
        public void testObservableCommand() throws Exception {
            HystrixObservableCommand<String> observableCommand = new ObservableCommandHelloWorld("observable world");
            observableCommand.toObservable().subscribe(new Observer<String>() {
                @Override
                public void onCompleted() {
                    System.out.println("complate");
                }
                @Override
                public void onError(Throwable throwable) {
                    System.out.println("error");
                }
                @Override
                public void onNext(String s) {
                    System.out.println("next:" + s);
                }
            });

            HystrixObservableCommand<String> observableCommand1 = new ObservableCommandHelloWorld("observable world1");
            String result = observableCommand1.observe().toBlocking().first();
            System.out.println(result);
        }
    }
}
