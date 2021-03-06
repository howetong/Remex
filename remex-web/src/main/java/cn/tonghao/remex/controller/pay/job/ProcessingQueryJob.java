package cn.tonghao.remex.controller.pay.job;

import cn.tonghao.remex.business.core.cache.CacheUtil;
import cn.tonghao.remex.business.core.log.RemexLogger;
import cn.tonghao.remex.business.pay.dto.TransDetailDTO;
import cn.tonghao.remex.business.pay.dto.query.QueryJobDTO;
import cn.tonghao.remex.business.pay.enums.CacheEnum;
import cn.tonghao.remex.business.pay.enums.PayChannelEnum;
import cn.tonghao.remex.business.pay.enums.StandardResponseCode;
import cn.tonghao.remex.business.pay.service.IQueryService;
import cn.tonghao.remex.common.util.DateTimeUtil;
import cn.tonghao.remex.common.util.JsonUtil;
import cn.tonghao.remex.common.util.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 定时查询任务
 * Created by howetong on 2018/5/8.
 */
@Component
public class ProcessingQueryJob {

    private static Logger logger = RemexLogger.getLogger(ProcessingQueryJob.class);

    private static final int DEFAULT_QUERY_LIMIT_NUM = 2000;
    private static final int DEFAULT_QUERY_MINUTE_BEFORE = 1;
    private static final int DEFAULT_QUERY_HOUR_INTERVAL = 24;

    @Resource
    private IQueryService queryService;

    @Resource
    private CacheUtil cacheUtil;

    /**
     * 查询方法
     * @param jsonStr 格式如：{"hourInterval": 144,"channelType":401,"limit": 1000}
     */
    public void execute(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            logger.error("ProcessingQueryJob-execute参数为空!");
            return;
        }
        Date requestTime = new Date();
        QueryJobDTO queryJobDTO = new QueryJobDTO();
        int hourInterval, minutesBeforeRequest;
        try {
            Map<String, String> map = JsonUtil.toMap(jsonStr);
            //查询的起始时间点（以当前为基准向前推）
            hourInterval = Integer.valueOf(map.get("hourInterval"));
            //查询的结束时间点（以当前为基准向前推）
            minutesBeforeRequest = Integer.valueOf(map.get("minutesBeforeRequest"));
            queryJobDTO.setChannelType(Integer.valueOf(map.get("channelType")));
            queryJobDTO.setRecordLimit(Integer.valueOf(map.get("limit")));
            String startTime = map.get("startDate");
            if (StringUtils.isNoneBlank(startTime)) {
                queryJobDTO.setStartTime(DateTimeUtil.getFormatDateTime(startTime));
            } else {
                queryJobDTO.setStartTime(DateTimeUtil.getDateBeforeOrAfterHours(requestTime, -1 * hourInterval));
            }
            String endTime = map.get("endDate");
            if (StringUtils.isNoneBlank(endTime)) {
                queryJobDTO.setEndTime(DateTimeUtil.getFormatDateTime(endTime));
            } else {
                queryJobDTO.setEndTime(DateTimeUtil.getDateBeforeOrAfterMinutes(requestTime, -1 * minutesBeforeRequest));
            }
        } catch (Exception e) {
            logger.info("参数解析异常，使用默认参数");
            queryJobDTO.setChannelType(PayChannelEnum.Baofoo.getChannelId());
            queryJobDTO.setRecordLimit(DEFAULT_QUERY_LIMIT_NUM);
            queryJobDTO.setStartTime(DateTimeUtil.getDateBeforeOrAfterHours(requestTime, -1 * DEFAULT_QUERY_HOUR_INTERVAL));
            queryJobDTO.setEndTime(DateTimeUtil.getDateBeforeOrAfterMinutes(requestTime, -1 * DEFAULT_QUERY_MINUTE_BEFORE));
        }
        queryJobDTO.setTotalRecordNum(queryService.countProcessingTrade(queryJobDTO));
        if (queryJobDTO.getTotalRecordNum() == 0) {
            logger.info("没有处理中的交易记录");
            return;
        }
        try {
            dealWithProcessingTrade(queryJobDTO);
        } catch (Exception e) {
            logger.error("ProcessingQueryJob-dealWithProcessingTrade exception:{}", e);
        }
    }

    private void dealWithProcessingTrade(QueryJobDTO queryJobDTO) {
        int totalPages = queryJobDTO.getTotalRecordNum() / queryJobDTO.getRecordLimit() + (queryJobDTO.getTotalRecordNum() % queryJobDTO.getRecordLimit() > 0 ? 1 : 0);
        List<TransDetailDTO> transDetailDTOList;
        int sucCount = 0, offSetValue = 0, totalProcessNum = 0;
        for (int pageStart = 0; pageStart < totalPages; pageStart++) {
            //设置偏移量
            queryJobDTO.setStartRecord(offSetValue);
            logger.info("ProcessingQueryJob-dealWithProcessingTrade job start");
            transDetailDTOList = queryService.ListProcessingTrade(queryJobDTO);
            if (!CollectionUtils.isEmpty(transDetailDTOList)) {
                //分页查询中累计查询成功的记录数
                sucCount = dealWithProcessTradeList(transDetailDTOList, sucCount);
                //分页查询中累计查询过的记录数
                totalProcessNum += transDetailDTOList.size();
                //分页查询中累计查询失败的记录数，作为下一次的偏移量
                offSetValue = totalProcessNum - sucCount;
            }
            logger.info("ProcessingQueryJob-queryProcessingTranDetail job #[{}] end, deal with [{}] records,[{}] records have done,[{}]records are processing.",
                    pageStart, queryJobDTO.getTotalRecordNum(), sucCount,
                    offSetValue);
        }
        logger.info("ProcessingQueryJob-queryProcessingTranDetail job finished, deal with [{}] records,[{}] records have done,[{}]records are processing.",
                queryJobDTO.getTotalRecordNum(), sucCount, offSetValue);
    }

    private int dealWithProcessTradeList(List<TransDetailDTO> transDetailDTOList, int sucCount) {
        for (TransDetailDTO transDetailDTO : transDetailDTOList) {
            logger.info("ProcessingQueryJob-dealWithProcessTradeList 开始处理记录out_trade_no[{}]", transDetailDTO.getOutTradeNo());
            Boolean lock = Boolean.TRUE;
            String cacheKey = CacheEnum.CASHIER_DICTIONARY_CODE.getKey(transDetailDTO.getOutTradeNo());
            if(!cacheUtil.lock(cacheKey)) {
                lock = Boolean.FALSE;
                logger.info("lock fail, outTradeNo is {}",transDetailDTO.getOutTradeNo());
            } else if (isDoneSingleProcessRemitRecord(transDetailDTO)) {
                sucCount++;
            }
            logger.info("ProcessingQueryJob-dealWithProcessTradeList out_trade_no[{}] sucCount:[{}]", transDetailDTO.getOutTradeNo(), sucCount);
            if(cacheKey != null && lock) {
                cacheUtil.delCache(cacheKey);
            }
        }
        return sucCount;
    }

    private boolean isDoneSingleProcessRemitRecord(TransDetailDTO transDetailDTO) {
        //处理单个交易中记录
        Response result = queryService.checkTradeDetailJob(transDetailDTO);
        logger.info("ProcessingQueryJob-isDoneSingleProcessRemitRecord out_trade_no[{}] result:[{}]",
                transDetailDTO.getOutTradeNo(), result.getErrorCode());
        if (null != result && result.getErrorCode() == StandardResponseCode.SUCCESS.getCode()) {
            return true;
        }
        return false;
    }


}
