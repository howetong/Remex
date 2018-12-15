package cn.tonghao.remex.business.test;

import cn.tonghao.remex.business.core.log.RemexLogger;
import org.slf4j.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author howetong
 * @Date 2018/12/15.
 */
public class TestJMSConsumer implements MessageListener {

    private static final Logger log = RemexLogger.getLogger(TestJMSConsumer.class);

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                log.info("收到业务消息:{}", ((TextMessage) message).getText());
                String msg = ((TextMessage) message).getText();
                System.out.println(msg);
            } catch (JMSException e) {
                log.warn("消息转换异常：{}", e);
            }
        }
    }
}
