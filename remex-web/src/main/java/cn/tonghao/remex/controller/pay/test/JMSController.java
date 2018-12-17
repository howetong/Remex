package cn.tonghao.remex.controller.pay.test;

import cn.tonghao.remex.business.test.ICommonMQService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author howetong
 * @Date 2018/12/15.
 */
@Controller
public class JMSController {

    @Resource
    private ICommonMQService commonMQService;

    @Value("${mq.queue.testName}")
    private String testQueueName;

    @RequestMapping(value = "/jmsTest")
    @ResponseBody
    public void doSomething() {
        String dto = "Hello World";
        commonMQService.sendMsg(testQueueName, dto);
    }
}
