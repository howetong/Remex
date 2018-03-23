package cn.tonghao.remex.controller.pay.api;

import cn.tonghao.remex.business.bill.CommonFileBillStrategy;
import cn.tonghao.remex.business.bill.IChannelFileBillStrategy;
import cn.tonghao.remex.business.core.drools.dto.Book;
import cn.tonghao.remex.business.core.drools.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by howetong on 2018/1/11.
 */
@Controller
public class TestController {

    @Autowired
    private BookService bookService;

    @Resource
    private IChannelFileBillStrategy channelFileBillStrategy;

    @RequestMapping("/test")
    @ResponseBody
    public String sayHello(){
        return "hello";
    }

    @RequestMapping("/testBill")
    public void getBill(){
        channelFileBillStrategy.getFillBill();
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
        Set routeSet = new HashSet();
        routeSet.add("fright");
        routeSet.add("discount");
        b.setRouteFactors(routeSet);
        Set<String> suppliers = bookService.selectBookSupplier(b);
        StringBuilder sb = new StringBuilder();
        for (String supplier : suppliers) {
            System.out.println(supplier);
            sb.append(supplier + ",");
        }
        return sb.toString();
    }


}
