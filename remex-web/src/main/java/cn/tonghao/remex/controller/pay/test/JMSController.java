package cn.tonghao.remex.controller.pay.test;

import cn.tonghao.remex.business.test.ICommonMQService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @author howetong
 * @Date 2018/12/15.
 */
@Controller
public class JMSController extends TestController {

    @Resource
    private ICommonMQService commonMQService;

    @Value("${queueName}")
    private String testQueueName;

    public void doSomething() {
        String dto = "Hello World";
        commonMQService.sendMsg(testQueueName, dto);
    }
}
