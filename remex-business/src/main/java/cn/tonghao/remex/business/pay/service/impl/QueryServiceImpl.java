package cn.tonghao.remex.business.pay.service.impl;

import cn.tonghao.remex.business.pay.dto.TransDetailDTO;
import cn.tonghao.remex.business.pay.dto.query.QueryJobDTO;
import cn.tonghao.remex.business.pay.service.IQueryService;
import cn.tonghao.remex.common.util.Response;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by howetong on 2018/5/14.
 */
@Service
public class QueryServiceImpl implements IQueryService{
    @Override
    public int countProcessingTrade(QueryJobDTO queryJobDTO) {
        return 0;
    }

    @Override
    public List<TransDetailDTO> ListProcessingTrade(QueryJobDTO queryJobDTO) {
        return null;
    }

    @Override
    public Response checkTradeDetailJob(TransDetailDTO tradeDetail) {
        return null;
    }
}
