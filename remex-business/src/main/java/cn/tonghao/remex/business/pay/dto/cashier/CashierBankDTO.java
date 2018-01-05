package cn.tonghao.remex.business.pay.dto.cashier;

/**
 * @Desc 支付方式布局下支持的支付卡
 * Created by howetong on 2018/1/5.
 */
public class CashierBankDTO {

    private String bankCode;
    private String bankDesc;
    private Integer cashierItemId;
    private Integer cardType;
    private Integer payMethod;
    private Integer payChannel;
    private Integer rank;
    private Integer dailyLimit;
    private Integer singleLimit;
    private boolean isActivity;
    private String activityContent;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankDesc() {
        return bankDesc;
    }

    public void setBankDesc(String bankDesc) {
        this.bankDesc = bankDesc;
    }

    public Integer getCashierItemId() {
        return cashierItemId;
    }

    public void setCashierItemId(Integer cashierItemId) {
        this.cashierItemId = cashierItemId;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public Integer getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Integer payChannel) {
        this.payChannel = payChannel;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(Integer dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public Integer getSingleLimit() {
        return singleLimit;
    }

    public void setSingleLimit(Integer singleLimit) {
        this.singleLimit = singleLimit;
    }

    public boolean isActivity() {
        return isActivity;
    }

    public void setActivity(boolean activity) {
        isActivity = activity;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }
}
