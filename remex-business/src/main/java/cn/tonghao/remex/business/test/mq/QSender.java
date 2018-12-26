package cn.tonghao.remex.business.test.mq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * @author howetong
 * @Date 2018/12/19.
 */
public class QSender {

    private ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private Destination queue;
    private MessageProducer producer;

    public QSender() {
        try {
            factory = new ActiveMQConnectionFactory("tcp://amq-master.sst.blackfi.sh:61616");
            connection = factory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            queue = session.createQueue("test");
            producer = session.createProducer(queue);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("初始化生产者失败");
        }
    }

    private void sendMessage(String text) {
        try {
            TextMessage message = session.createTextMessage(text);
            producer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
            System.err.println("发送消息失败，生产者做重发处理");
        }
    }

    private void exit() {
        try {
            if (connection != null) {
                connection.close();
            }
            if (session != null) {
                session.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        QSender sender = new QSender();
        String message = "test消息";
        System.out.println("准备发送消息：" + message);
        sender.sendMessage(message);
        System.out.println("消息已发送");
        sender.exit();
    }

}