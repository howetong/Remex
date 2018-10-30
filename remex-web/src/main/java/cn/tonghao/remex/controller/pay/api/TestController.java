package cn.tonghao.remex.controller.pay.api;

import cn.tonghao.remex.business.core.drools.dto.Book;
import cn.tonghao.remex.business.core.drools.service.BookService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * 测试控制器
 * Created by howetong on 2018/1/11.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Resource
    private BookService bookService;

    @RequestMapping("/")
    @ResponseBody
    public String sayHello(){
        return "hello";
    }


    @RequestMapping("/hystrix")
    @HystrixCommand(groupKey = "myGroup", commandKey = "myCommand", fallbackMethod = "fallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "50"),//方法执行超时时间
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),//当并发错误个数达到此阀值时(在时间窗口内)，触发隔断器
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "60000"),//滚动窗口时间长度
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),//滚动窗口的桶数
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "60000"),//隔断器被触发后，睡眠多长时间开始重试请求
            },
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "20"),
                    @HystrixProperty(name = "maxQueueSize", value = "100"),
            })
    public void testHystrix(){
        hystrix();
    }



    @RequestMapping(value = "/order")
    @ResponseBody
    public String orderBook(HttpServletRequest request){
        Book b = new Book();
        b.setBasePrice(120.50);
        b.setClz("computer");
        b.setName("C plus programing");
        b.setSalesArea("china");
        b.setYears(2);

        double realPrice = bookService.getBookSalePrice(b);
        System.out.println(b.getName() + ":" + realPrice);
        return "name:" + b.getName() + ". price:" + b.getSalesPrice();
    }

    @RequestMapping(value = "/supplier")
    @ResponseBody
    public String selectPublishier(HttpServletRequest request){
        Book b = new Book();
        b.setName("Java核心思想");
        b.setBasePrice(52);
        b.setDiscount("0.92");
        Set<String> routeSet = new HashSet<>();
        routeSet.add("fright");
        routeSet.add("discount");
        b.setRouteFactors(routeSet);
        Set<String> suppliers = bookService.selectBookSupplier(b);
        StringBuilder sb = new StringBuilder();
        for (String supplier : suppliers) {
            System.out.println(supplier);
            sb.append(supplier).append(",");
        }
        return sb.toString();
    }


    private void hystrix() {
        System.out.println(Thread.currentThread().getName());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 100; i++) {
            System.out.println(i);
        }
    }

}
