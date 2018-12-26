package cn.tonghao.remex.business.test.mq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.io.IOException;

/**
 * @author howetong
 * @Date 2018/12/19.
 */
public class QReceiverListener implements MessageListener {

    private ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private Destination queue;
    private MessageConsumer consumer;

    public QReceiverListener() {
        try {
            factory = new ActiveMQConnectionFactory("tcp://amq-master.sst.blackfi.sh:61616");
            connection = factory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            queue = session.createQueue("test");
            consumer = session.createConsumer(queue);
            consumer.setMessageListener(this);
            System.out.println("等待接受消息......");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("消息内容：" + textMessage.getText() + ",是否重发：" + textMessage.getJMSRedelivered());
            throw new RuntimeException("test");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new QReceiverListener();
    }

}