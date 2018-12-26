package cn.tonghao.remex.business.pay.dto.query;

import java.util.Date;

/**
 * Created by howetong on 2018/5/12.
 */
public class QueryJobDTO {

    private Integer channelType;
    private Date startTime;
    private Date endTime;
    private Integer startRecord;
    private Integer recordLimit;
    private Integer totalRecordNum;

    public Integer getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStartRecord() {
        return startRecord;
    }

    public void setStartRecord(Integer startRecord) {
        this.startRecord = startRecord;
    }

    public Integer getRecordLimit() {
        return recordLimit;
    }

    public void setRecordLimit(Integer recordLimit) {
        this.recordLimit = recordLimit;
    }

    public Integer getTotalRecordNum() {
        return totalRecordNum;
    }

    public void setTotalRecordNum(Integer totalRecordNum) {
        this.totalRecordNum = totalRecordNum;
    }
}
