package cn.tonghao.remex.business.core.drools.util;

import cn.tonghao.remex.business.core.drools.dto.DecisionDTO;
import cn.tonghao.remex.business.pay.enums.PayChannelEnum;
import cn.tonghao.remex.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by howetong on 2018/3/5.
 */
public class RuleTools {

    private static Logger logger = LoggerFactory.getLogger(RuleTools.class);

//    public static Set getExChannelByMaintaiTime(DecisionDTO decisionDTO){
//        Map<String, Map<String, Object>> basicRouteMap = decisionDTO.getBasicFators();
//        Set<PayChannelEnum> mainTainChanels = new HashSet<>();
//        Map<String, Object> mainTainMap = basicRouteMap.get("maintainTime");
//        long now = new Date().getTime();//当前时间
//        if (mainTainMap != null) {
//            for (Map.Entry<String, Object> entrySet : mainTainMap.entrySet()) {
//                if (entrySet.getValue() != null) {
//                    String mainTainTime = entrySet.getValue().toString();
//                    String[] str = mainTainTime.split(",");
//                    long start, end;
//                    try {
//                        start = new SimpleDateFormat("yyyyMMddHHmmss").parse(str[0]).getTime();
//                        end = new SimpleDateFormat("yyyyMMddHHmmss").parse(str[1]).getTime();
//                        if (now > start && now < end) {
//                            int channelId = Integer.valueOf(entrySet.getKey());
//                            logger.info("渠道" + PayChannelEnum.getPayChannelById(channelId).getName() + "正在维护中");
//                            mainTainChanels.add(PayChannelEnum.getPayChannelById(channelId));
//                        }
//                    } catch (ParseException e) {
//                        logger.error("日期转换错误");
//                    }
//                }
//            }
//        }
//        return mainTainChanels;
//    }

    public static boolean isEmpty(String str){
        return StringUtils.isEmpty(str);
    }

    public static void printTest(String str){
        System.out.println(str);
    }
}
