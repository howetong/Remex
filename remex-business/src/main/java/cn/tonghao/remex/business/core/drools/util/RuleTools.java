package cn.tonghao.remex.business.core.drools.util;

import cn.tonghao.remex.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by howetong on 2018/3/5.
 */
public class RuleTools {

    private static Logger logger = LoggerFactory.getLogger(RuleTools.class);

    public static boolean isEmpty(String str){
        return StringUtils.isEmpty(str);
    }

    public static void printTest(String str){
        System.out.println(str);
    }
}
