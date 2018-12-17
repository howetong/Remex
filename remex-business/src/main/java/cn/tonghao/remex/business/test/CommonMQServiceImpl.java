package cn.tonghao.remex.business.test;

import cn.tonghao.remex.common.util.JsonUtil;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author howetong
 * @Date 2018/12/15.
 */
@Service
public class CommonMQServiceImpl implements ICommonMQService {
    @Resource
    private JmsTemplate jmsTemplate;

    public void sendMsg(String queueName, Object data) {
        try {
            final String msg = JsonUtil.toString(data);
            jmsTemplate.send(queueName, session -> session.createTextMessage(msg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}