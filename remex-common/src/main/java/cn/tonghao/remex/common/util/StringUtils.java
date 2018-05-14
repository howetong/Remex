package cn.tonghao.remex.common.util;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 过滤特殊字符串
     * @Param source 原字符串
     * @Param slipStr emoji表情替换后的符号
     * @Return String 过滤掉emoji表情后的字符串
     */
    public static String filterEmoji(String source, String slipStr) {
        if(StringUtils.isNotBlank(source)){
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", slipStr);
        }else{
            return source;
        }
    }

}
