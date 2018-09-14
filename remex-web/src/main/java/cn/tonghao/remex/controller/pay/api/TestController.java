package cn.tonghao.remex.controller.pay.api;

import cn.tonghao.remex.business.core.drools.dto.Book;
import cn.tonghao.remex.business.core.drools.service.BookService;
import cn.tonghao.remex.common.util.DefaultKeyGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

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


    public static void main(String[] args) {
        DefaultKeyGenerator defaultKeyGenerator = DefaultKeyGenerator.getInstance();
        List<String> arrayList = new ArrayList<>();
        for (int i = 0; i < 1800; i++) {
            arrayList.add(defaultKeyGenerator.generateUniqueId("1001", "00"));
        }
        for (int i = 0; i < arrayList.size() - 1; i++) {
            String temp = arrayList.get(i);
            String next = arrayList.get(i + 1);
            if (temp.equals(next)) {
                System.out.println(temp);
            }
            System.out.println(temp);
        }
    }

}
