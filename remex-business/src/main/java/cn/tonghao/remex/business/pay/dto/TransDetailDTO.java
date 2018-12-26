package cn.tonghao.remex.business.pay.dto;


import cn.tonghao.remex.common.util.jackson.deserialize.JackJsonDateTimeParse;
import cn.tonghao.remex.common.util.jackson.serialize.JackJsonDateTimeFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by howetong on 2018/5/12.
 */
public class TransDetailDTO {

    private Integer id;

    private Integer bizOrderId;

    private Integer bizId;

    private String outTradeNo;

    private Integer channelType;

    private BigDecimal amount;

    private String partnerOrderId;

    private String partnerBatchId;

    private BigDecimal partnerTransFee;

    private Integer status;

    private String respMsg;

    private Date addTime;

    private Date updateTime;

    @JsonSerialize(using = JackJsonDateTimeFormat.class)
    @JsonDeserialize(using = JackJsonDateTimeParse.class)
    private Date transApplyTime;

    @JsonSerialize(using = JackJsonDateTimeFormat.class)
    @JsonDeserialize(using = JackJsonDateTimeParse.class)
    private Date transCompleteTime;

    private Integer partnerId;

    private Integer platformId;

    private Integer addUid;

    private Integer updateUid;

    private String comment;

    private String remark;

    private Integer delFlag;

    private String toAccNo;

    private String toAccName;

    private String toAccId;

    private String toAccMobile;

    private String toAccBank;

    private String toAccMemberId;

    private String tradeNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBizOrderId() {
        return bizOrderId;
    }

    public void setBizOrderId(Integer bizOrderId) {
        this.bizOrderId = bizOrderId;
    }

    public Integer getBizId() {
        return bizId;
    }

    public void setBizId(Integer bizId) {
        this.bizId = bizId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPartnerOrderId() {
        return partnerOrderId;
    }

    public void setPartnerOrderId(String partnerOrderId) {
        this.partnerOrderId = partnerOrderId;
    }

    public String getPartnerBatchId() {
        return partnerBatchId;
    }

    public void setPartnerBatchId(String partnerBatchId) {
        this.partnerBatchId = partnerBatchId;
    }

    public BigDecimal getPartnerTransFee() {
        return partnerTransFee;
    }

    public void setPartnerTransFee(BigDecimal partnerTransFee) {
        this.partnerTransFee = partnerTransFee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getTransApplyTime() {
        return transApplyTime;
    }

    public void setTransApplyTime(Date transApplyTime) {
        this.transApplyTime = transApplyTime;
    }

    public Date getTransCompleteTime() {
        return transCompleteTime;
    }

    public void setTransCompleteTime(Date transCompleteTime) {
        this.transCompleteTime = transCompleteTime;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public Integer getAddUid() {
        return addUid;
    }

    public void setAddUid(Integer addUid) {
        this.addUid = addUid;
    }

    public Integer getUpdateUid() {
        return updateUid;
    }

    public void setUpdateUid(Integer updateUid) {
        this.updateUid = updateUid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getToAccNo() {
        return toAccNo;
    }

    public void setToAccNo(String toAccNo) {
        this.toAccNo = toAccNo;
    }

    public String getToAccName() {
        return toAccName;
    }

    public void setToAccName(String toAccName) {
        this.toAccName = toAccName;
    }

    public String getToAccId() {
        return toAccId;
    }

    public void setToAccId(String toAccId) {
        this.toAccId = toAccId;
    }

    public String getToAccMobile() {
        return toAccMobile;
    }

    public void setToAccMobile(String toAccMobile) {
        this.toAccMobile = toAccMobile;
    }

    public String getToAccBank() {
        return toAccBank;
    }

    public void setToAccBank(String toAccBank) {
        this.toAccBank = toAccBank;
    }

    public String getToAccMemberId() {
        return toAccMemberId;
    }

    public void setToAccMemberId(String toAccMemberId) {
        this.toAccMemberId = toAccMemberId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }
}
