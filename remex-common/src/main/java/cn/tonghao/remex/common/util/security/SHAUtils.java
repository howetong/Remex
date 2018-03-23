package cn.tonghao.remex.common.util.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA256摘要工具类
 */
public class SHAUtils {

    private static final Logger logger = LoggerFactory.getLogger(SHAUtils.class);
    private static final String DEFAULT_SHA_NAME = "SHA-256";

    /**
     * 对待加密字符串采用256算法加密
     *
     * @param signData 待加密信息
     * @return
     */
    public static String SHA256Encrypt(String signData) {
        return Encrypt(signData, DEFAULT_SHA_NAME);
    }

    /**
     * 对字符串加密,加密算法使用MD5,SHA-1,SHA-256,默认使用SHA-256
     *
     * @param strSrc  要加密的字符串
     * @param encName 加密类型
     * @return
     */
    public static String Encrypt(String strSrc, String encName) {
        byte[] bytes = new byte[0];
        try {
            bytes = strSrc.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("SHA encrypt unsupport encode :", e);
            return StringUtils.EMPTY;
        }
        String strDes = "";
        try {
            if (StringUtils.isEmpty(encName)) {
                encName = DEFAULT_SHA_NAME;
            }
            MessageDigest md = MessageDigest.getInstance(encName);
            md.update(bytes);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            logger.error("SHA encrypt unsupport algorithm :", e);
            return StringUtils.EMPTY;
        }
        return strDes;
    }

    public static String bytes2Hex(byte[] bytes) {
        StringBuilder strBuilder = new StringBuilder(64);
        for (int i = 0, len = bytes.length; i < len; i++) {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if (hex.length() == 1) {
                strBuilder.append('0');
            }
            strBuilder.append(hex);
        }
        return strBuilder.toString();
    }
}
