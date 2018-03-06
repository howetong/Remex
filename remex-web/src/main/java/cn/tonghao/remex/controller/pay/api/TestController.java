package cn.tonghao.remex.controller.pay.api;

import cn.tonghao.remex.business.core.drools.dto.Book;
import cn.tonghao.remex.business.core.drools.dto.DecisionDTO;
import cn.tonghao.remex.business.core.drools.dto.PayRequestDTO;
import cn.tonghao.remex.business.core.drools.service.BookService;
import cn.tonghao.remex.business.core.drools.service.PayChannelSelecorService;
import cn.tonghao.remex.business.pay.enums.PayChannelEnum;
import cn.tonghao.remex.common.annotation.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by howetong on 2018/1/11.
 */
@Controller
public class TestController {

    @Autowired
    private BookService bookService;

    @Resource
    private PayChannelSelecorService payChannelSelecorService;

    @RequestMapping("/test")
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

    @RequestMapping(value = "/channel")
    @ResponseBody
    public void selectChannel(@Json PayRequestDTO payRequestDTO){
        //请求参数中含有业务id，支付的银行卡和金额
        //根据业务id和支付银行卡，查询配置的路由规则routerFactor
        /**假设本次支付的渠道有5个，分别是宝付，支付宝，微信，易宝和畅捷
         * 查询到的基本路由因子：银行维护时间
         * 查询到的补充路由因子：单笔限额和支付金额
         */
        //初始支持的渠道
        Set<PayChannelEnum> channelSet = new HashSet<>();
        channelSet.add(PayChannelEnum.Alipay);
        channelSet.add(PayChannelEnum.Baofoo);
        channelSet.add(PayChannelEnum.ChanPay);
        channelSet.add(PayChannelEnum.Wechat);
        channelSet.add(PayChannelEnum.YeePay);
        //基本路由因子
        Set<String> basicFactorsSet = new HashSet<>();
        basicFactorsSet.add("maintainTime");
        //补充路由因子
        Set<String> routeFactorsSet = new HashSet<>();
        routeFactorsSet.add("singleLimit");
        routeFactorsSet.add("amountLimit");

        //基本路由因子具体规则，假设查询到的维护时间如下
        Map<String, Object> maintainTimeMap = new HashMap<>();
        maintainTimeMap.put("119",null);
        maintainTimeMap.put("137",null);
        maintainTimeMap.put("136",null);
        maintainTimeMap.put("135","201803051100,201803051300");
        maintainTimeMap.put("139","201803052230,201803052300");

        //补充路由因子1规则,假设查询到的5个渠道的单笔限额如下
        Map<String, String> singleLimitMap = new HashMap<>();
        singleLimitMap.put("119","10000");
        singleLimitMap.put("137","100000");
        singleLimitMap.put("136","100000");
        singleLimitMap.put("135","6000");
        singleLimitMap.put("139","3000");
        //补充路由因子2规则,假设查询到的支付金额限制如下
        Map<String, String> amountLimitMap = new HashMap<>();
        amountLimitMap.put("139","0,1000");//不大于1000的走畅捷
        amountLimitMap.put("135","1000,3000");//大于1000，不大于3000的走易宝
        amountLimitMap.put("119","3000,10000");//大于3000，不大于10000的走宝付
        amountLimitMap.put("136","10000,100000");//大于10000的走支付宝或者微信
        amountLimitMap.put("137","10000,100000");

        //补充路由因子优先级
        Map<String, String> priorityMap = new HashMap<>();
        priorityMap.put("singleLimit","1");
        priorityMap.put("amountLimit","2");

        //决策模型实体
        DecisionDTO decisionDTO = new DecisionDTO();
        //初始渠道
        decisionDTO.setPayChannelSet(channelSet);
        //支付金额
        decisionDTO.setAmount(payRequestDTO.getAmount());
        //基本决策因子
        Map<String, Map<String,Object>> basicFactorsMap = new HashMap<>();
        basicFactorsMap.put("maintainTime",maintainTimeMap);
        decisionDTO.setBasicFators(basicFactorsMap);
        //补充决策因子
        Map<String, Object> routeFactorsMap = new HashMap<>();
        routeFactorsMap.put("singleLimit",singleLimitMap);
        routeFactorsMap.put("amountLimit",amountLimitMap);
        decisionDTO.setRouteFators(routeFactorsMap);
        //决策因子优先级
        decisionDTO.setPriorityMap(priorityMap);
        Set<PayChannelEnum> payChannelEnums = payChannelSelecorService.selectPayChannel(decisionDTO);
        for (PayChannelEnum payChannelEnum : payChannelEnums) {
            System.out.println(payChannelEnum.getName());
        }
    }
}
