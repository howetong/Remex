package cn.tonghao.remex.business.core.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.NumberFormat;
import java.util.Date;

/**
 * Created by howetong on 2018/1/5.
 */
public class SerialNoGenUtils {

    public static String geneOutTradNo(int bizId, int suffix) {
        int rand = (int) (Math.random() * 10000);
        String randStr = String.format("%04d", rand);
        /**
         * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
         */
        StringBuffer buffer = new StringBuffer();
        return  buffer.append(DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS"))
                .append(fillNum(bizId,10))
                .append(randStr)
                .append(suffix)
                .toString();
    }


    /**
     * 字符填充
     * @param data
     * @param len 目标长度
     * @return
     */
    private static String  fillNum(int data,int len){
        //得到一个NumberFormat的实例
        NumberFormat nf = NumberFormat.getInstance();
        //设置是否使用分组
        nf.setGroupingUsed(false);
        //设置最小整数位数
        nf.setMinimumIntegerDigits(len);
        return nf.format(data);
    }
}
