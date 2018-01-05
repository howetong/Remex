package cn.tonghao.remex.business.pay.dto.cashier;

import cn.tonghao.remex.business.pay.dto.cashier.CashierBankDTO;

import java.util.List;

/**
 * Created by howetong on 2018/1/2.
 */
public class CashierLayoutDTO {

    /**
     * 收银台-布局映射id
     */
    private Integer layoutId;

    /**
     * 业务收银台id（一个业务收银台对应多个支付方式布局）
     */
    private Integer cashierId;

    /**
     * 支付方式布局（支付方式收银台）id
     */
    private Integer cashierItemId;

    /**
     * 支付方式布局是否启用
     */
    private Integer status;

    /**
     * 支付方式布局显示顺序
     */
    private Integer rank;


    /**
     * 支付方式布局对应的支付方法
     */
    private Integer payMethod;

    /**
     * 支付方式布局名称
     */
    private String layoutName;

    /**
     * 支付方式布局描述
     */
    private String layoutDesc;

    /**
     * 支付方式布局支持的所有支付卡
     */
    private List<CashierBankDTO> cashierBanks;

    private String activityContent;

    public Integer getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Integer layoutId) {
        this.layoutId = layoutId;
    }

    public Integer getCashierId() {
        return cashierId;
    }

    public void setCashierId(Integer cashierId) {
        this.cashierId = cashierId;
    }

    public Integer getCashierItemId() {
        return cashierItemId;
    }

    public void setCashierItemId(Integer cashierItemId) {
        this.cashierItemId = cashierItemId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public String getLayoutDesc() {
        return layoutDesc;
    }

    public void setLayoutDesc(String layoutDesc) {
        this.layoutDesc = layoutDesc;
    }

    public List<CashierBankDTO> getCashierBanks() {
        return cashierBanks;
    }

    public void setCashierBanks(List<CashierBankDTO> cashierBanks) {
        this.cashierBanks = cashierBanks;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }
}
