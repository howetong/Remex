package cn.tonghao.remex.business.pay.dto.cashier;

import cn.tonghao.remex.business.core.aop.ObjectNotNull;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by howetong on 2018/1/5.
 */
@ObjectNotNull
public class CashierDetailLayoutReqDTO {
    /**
     * 业务订单号
     **/
    @NotBlank
    private String bizOrderId;

    /**
     * 平台版本ID
     **/
    @NotNull
    private Integer platformId;

    /**
     * 业务ID
     **/
    @NotNull
    private Integer bizId;

    /**
     * SDK版本号
     */
    @NotBlank
    private String sdkVersion;

    /**
     * 签名
     **/
    @NotBlank
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
