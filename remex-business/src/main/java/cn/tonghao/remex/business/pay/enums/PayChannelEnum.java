package cn.tonghao.remex.business.pay.enums;

/**
 * Created by howetong on 2018/3/2.
 */
public enum PayChannelEnum {
    Baofoo(119,"宝付"),
    YeePay(135, "易宝"),
    Alipay(136, "支付宝"),
    Wechat(137, "微信"),
    ChanPay(139, "畅捷");

    private int channelId;
    private String name;

    PayChannelEnum(int channelId, String name) {
        this.channelId = channelId;
        this.name = name;
    }

    public static PayChannelEnum getPayChannelById(int id){
        for (PayChannelEnum payChannelEnum : PayChannelEnum.values()) {
            if (id == payChannelEnum.getChannelId()) {
                return payChannelEnum;
            }
        }
        return null;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
