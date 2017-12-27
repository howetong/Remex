package cn.tonghao.remex.common.core.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;


/**
 * 将String进行base64编码解码，使用utf-8
 */

public class Base64Util {

    private static final Logger LOG = LoggerFactory.getLogger(Base64Util.class);

    /**
     * 对给定的字符串进行base64解码操作
     *
     * @param input : 待解码的字符串
     * @return 解码后的字符串
     */
    public static String decode(String input) {
        try {
            return new String(Base64.decodeBase64(input.getBytes("utf-8")), "utf-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error(input, e);
        }

        return null;
    }

    /**
     * 对给定的字符串进行base64压码操作
     *
     * @param input 待压码的字符串
     * @return 压码后的字符串
     */
    public static String encode(String input) {
        try {
            return new String(Base64.encodeBase64(input.getBytes("utf-8")), "utf-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error(input, e);
        }

        return null;
    }
}
