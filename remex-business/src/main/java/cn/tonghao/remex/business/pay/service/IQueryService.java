package cn.tonghao.remex.business.pay.service;

import cn.tonghao.remex.business.pay.dto.TransDetailDTO;
import cn.tonghao.remex.business.pay.dto.query.QueryJobDTO;
import cn.tonghao.remex.common.util.Response;

import java.util.List;

/**
 * Created by howetong on 2018/5/12.
 */
public interface IQueryService {

    /**
     * 根据queryJobDTO统计处理中的交易总数
     * @param queryJobDTO 统计指标对象，包含统计指标，如渠道，时间
     */
    int countProcessingTrade(QueryJobDTO queryJobDTO);

    /**
     * 根据queryJobDTO获取处理中的交易实体list
     * @param queryJobDTO 统计指标对象，包含统计指标，如渠道，时间
     */
    List<TransDetailDTO> ListProcessingTrade(QueryJobDTO queryJobDTO);

    /**
     * 处理单个交易中的记录
     * @param tradeDetail 处理中记录实体
     * @return 返回处理结果
     */
    Response checkTradeDetailJob(TransDetailDTO tradeDetail);
}

