package cn.tonghao.remex.business.pay.dto.cashier;

/**
 * Created by howetong on 2018/1/5.
 */
public class CashierDetailLayoutReqDTO {
    /**
     * 业务订单号
     **/
    private String bizOrderId;

    /**
     * 平台版本ID
     **/
    private Integer platformId;
    /**
     * 业务ID
     **/
    private Integer bizId;

    /**
     * SDK版本号
     */
    private String sdkVersion;

    /**
     * 签名
     **/
    private String sign;

    public String getBizOrderId() {
        return bizOrderId;
    }

    public void setBizOrderId(String bizOrderId) {
        this.bizOrderId = bizOrderId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public Integer getBizId() {
        return bizId;
    }

    public void setBizId(Integer bizId) {
        this.bizId = bizId;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }
}
