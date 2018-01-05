package cn.tonghao.remex.common.util;

/**
 * Created by howetong on 2018/1/5.
 */
public class MessageConverterTpyeUtil {
    public static final int MESSAGE_CONVERTER_TYPE_JSON = 2;
    public static final int MESSAGE_CONVERTER_TYPE_JSON_BASE64 = 3;
    public static final int MESSAGE_CONVERTER_TYPE_UNDEFINE = 3;

    public MessageConverterTpyeUtil() {
    }

    public static int getMessageConverterTypeFromHttpHead(String httpRequestDataType) {
        int ret = 3;
        if(httpRequestDataType != null && !httpRequestDataType.isEmpty()) {
            if(httpRequestDataType.equalsIgnoreCase("json/text")) {
                ret = 2;
            } else if(httpRequestDataType.equalsIgnoreCase("json/base64")) {
                ret = 3;
            }
        } else {
            ret = 3;
        }

        return ret;
    }
}