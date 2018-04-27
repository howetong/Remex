package cn.tonghao.remex.business.core.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by howetong on 2018/1/5.
 */
public class SerialNoGenUtils {

    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "-", "_" };

    public static String gen30UUID() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        // 每3个十六进制字符转换成为2个字符
        for (int i = 0; i < 10; i++) {
            String str = uuid.substring(i * 3, i * 3 + 3);
            //转成十六进制
            int x = Integer.parseInt(str, 16);
            //除64得到前面6个二进制数的
            shortBuffer.append(chars[x / 0x40]);
            // 对64求余得到后面6个二进制数1
            shortBuffer.append(chars[x % 0x40]);
        }
        //加上后面两个没有改动的
        shortBuffer.append(uuid.charAt(30));
        shortBuffer.append(uuid.charAt(31));
        //加上8位随机数
        Random random=new Random();
        //随机生成数字，并添加到字符串
        for(int i=0;i<8;i++){
            shortBuffer.append(random.nextInt(10));
        }
        return shortBuffer.toString();
    }

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
