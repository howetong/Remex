package cn.tonghao.remex.controller.pay.job;

import cn.tonghao.remex.business.bill.IChannelFileBillStrategy;
import cn.tonghao.remex.business.core.log.RemexLogger;
import cn.tonghao.remex.business.pay.enums.PayChannelEnum;
import cn.tonghao.remex.business.pay.strategy.ChannelStrategyFactory;
import cn.tonghao.remex.common.util.JsonUtil;
import cn.tonghao.remex.common.util.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 对账单下载任务
 * Created by howetong on 2018/9/14.
 */
@Component
public class FileBillJob {

    private static final Logger logger = RemexLogger.getLogger(FileBillJob.class);

    //@RequestMapping("/getBill/{channelId}")
    public void getBill(String jsonStr){
        String channelIds = String.valueOf(PayChannelEnum.Baofoo.getChannelId());
        if (StringUtils.isNoneBlank(jsonStr)) {
            Map<String, String> reqParamMap = JsonUtil.toMap(jsonStr);
            channelIds = reqParamMap.get("channelIds");
        }
        String[] channelArray = channelIds.split(",");
        for (String channelId : channelArray) {
            IChannelFileBillStrategy channelFileBillStrategy = getBillChannel(Integer.valueOf(channelId));
            if (channelFileBillStrategy == null) {
                logger.error("不支持从当前渠道获取对账单，渠道id：{}", channelId);
                return;
            }
            channelFileBillStrategy.getFillBill();
        }
    }

    private IChannelFileBillStrategy getBillChannel(int channelId) {
        BeanFactory factory = ChannelStrategyFactory.getBeanFactory();
        PayChannelEnum channelEnum = PayChannelEnum.getPayChannelById(channelId);
        if (channelEnum == null) {
            return null;
        }
        switch (channelEnum) {
            case Baofoo:
                return (IChannelFileBillStrategy) factory.getBean("baofooFileBillStrategy");
            case Wechat:
                return (IChannelFileBillStrategy) factory.getBean("wechatFileBillStrategy");
            default:
                return null;
        }
    }
}
