package cn.tonghao.remex.controller.pay.api;

import cn.tonghao.remex.business.bill.IChannelFileBillStrategy;
import cn.tonghao.remex.business.core.drools.dto.Book;
import cn.tonghao.remex.business.core.drools.service.BookService;
import cn.tonghao.remex.business.core.log.RemexLogger;
import cn.tonghao.remex.business.pay.enums.PayChannelEnum;
import cn.tonghao.remex.business.pay.strategy.ChannelStrategyFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

    private static final Logger logger = RemexLogger.getLogger(TestController.class);

    @Resource
    private BookService bookService;

    @RequestMapping("/")
    @ResponseBody
    public String sayHello(){
        return "hello";
    }

    @RequestMapping("/getBill/{channelId}")
    public void getBill(@PathVariable("channelId") int channelId){
        IChannelFileBillStrategy channelFileBillStrategy = getBillChannel(channelId);
        if (channelFileBillStrategy == null) {
            logger.error("不支持从当前渠道获取对账单，渠道id：{}", channelId);
            return;
        }
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

    private IChannelFileBillStrategy getBillChannel(int channelId) {
        BeanFactory factory = ChannelStrategyFactory.getBeanFactory();
        PayChannelEnum channelEnum = PayChannelEnum.getPayChannelById(channelId);
        if (channelEnum == null) {
            return null;
        }
        switch (channelEnum) {
            case Baofoo:
                return (IChannelFileBillStrategy) factory.getBean("baofooFileBillStrategy");
            case Wechat:
                return (IChannelFileBillStrategy) factory.getBean("wechatFileBillStrategy");
            default:
                return null;
        }
    }


}
