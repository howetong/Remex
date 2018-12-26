package cn.tonghao.remex.business.test.mq;

/**
 * @author howetong
 * @Date 2018/12/15.
 */
public interface ICommonMQService {

    /**
     * 发送消息
     */
    void sendMsg(String queueName, Object data);


}