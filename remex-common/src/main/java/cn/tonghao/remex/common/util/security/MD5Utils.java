package cn.tonghao.remex.common.util.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.SignatureException;

/**
 * MD5摘要生成和摘要验证工具类
 */
public class MD5Utils {

    /**
     * MD5直接生成摘要
     * @param content 摘要源串
     * @param charset 字符集，如不指定，则使用系统默认字符集
     * @return String MD5摘要值
     * @throws Exception 签名异常
     */
    public static String md5Direct(String content, String charset) throws Exception {
        try {
            return DigestUtils.md5Hex(getContentBytes(content, charset));
        } catch (UnsupportedEncodingException e) {
            throw new SignatureException(" MD5 Exception [content = " + content + "; charset = " + charset + "]Exception!", e);
        }
    }

    /**
     * MD5直接生成摘要，默认使用UTF-8编码
     * @param content 摘要源串
     * @return String摘要
     */
    public static String md5Direct(String content) {
        if (content == null)
            return null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(content.getBytes("UTF-8"));
            byte[] digest = md5.digest();
            StringBuffer hexString = new StringBuffer();
            String strTemp;
            for (int i = 0; i < digest.length; i++) {
                strTemp = Integer.toHexString((digest[i] & 0x000000FF) | 0xFFFFFF00).substring(6);
                hexString.append(strTemp);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * MD5直接生成摘要，在content中加上key作为盐
     * @param content 摘要源串（不含盐）
     * @param key 盐
     * @return String 摘要值
     * @throws Exception
     */
    public static String md5WithSalt(String content, String key) throws Exception {
        return md5WithSalt(content, key, "UTF-8");
    }

    public static String md5WithSalt(String content, String key, String charset) throws Exception {
        String toSign = (content == null ? "" : content) + key;
        try {
            return DigestUtils.md5Hex(getContentBytes(toSign, charset));
        } catch (UnsupportedEncodingException e) {
            throw new SignatureException(" MD5 Exception [content = " + content + "; charset = " + charset + "]Exception!", e);
        }
    }

    /**
     * 摘要验证
     * @param content 不含盐的源串
     * @param sign 待验证的摘要
     * @param key 盐
     * @return true 验证通过； false 验证失败
     * @throws Exception
     */
    public static boolean validate(String content, String sign, String key) throws Exception {
        return validate(content, sign, key, "UTF-8");
    }

    public static boolean validate(String content, String sign, String key, String charset) throws Exception {
        String tosign = (content == null ? "" : content) + key;
        try {
            String mySign = DigestUtils.md5Hex(getContentBytes(tosign, charset));
            return StringUtils.equals(mySign, sign) ? true : false;
        } catch (UnsupportedEncodingException e) {
            throw new SignatureException("MD5Exception[content = " + content + "; charset =" + charset + "; signature = " + sign + "]Exception!", e);
        }
    }

    protected static byte[] getContentBytes(String content, String charset) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(charset)) {
            return content.getBytes();
        }
        return content.getBytes(charset);
    }
}
