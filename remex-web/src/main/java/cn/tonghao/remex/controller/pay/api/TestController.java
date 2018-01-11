package cn.tonghao.remex.controller.pay.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by howetong on 2018/1/11.
 */
@Controller
public class TestController {

    @RequestMapping("/test")
    @ResponseBody
    public String sayHello(){
        return "hello";
    }
}
