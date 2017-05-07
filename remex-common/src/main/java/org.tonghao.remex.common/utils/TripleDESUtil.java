package org.tonghao.remex.common.utils;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;

/**
 * 3DES加密工具
 * Created by Administrator on 2017/5/7 0007.
 */
public class TripleDESUtil {

    private static String Algorithm = "DESede";// 定义 加密算法,可用DES,DESede,Blowfish

    /**
     * 使用3DES对字符串加密，并使用Base64进行转码
     * @param string：要加密的字符串
     * @param key：密钥
     * @return
     */
    public static String encryptMode(String string,String key){
        //把要加密的字符串转换成字节数组类型
        byte[] src = string.getBytes();
        try{
            //生成密钥
            SecretKey deskey = new SecretKeySpec(key.getBytes(),Algorithm);
            //加密,注意这里需要注明分组及补位方式。否则解密时可能导致补位方式不同而报错
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,deskey);
            byte[] encryptBytes = cipher.doFinal(src);
            byte[] encodeEncryptBytes = Base64.encodeBase64(encryptBytes);
            return new String(encodeEncryptBytes);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }


    public static String decryptMode(String string,String key){
        //把要解密的字符串先解码
        byte[] decodeString = Base64.decodeBase64(string.getBytes());
        try{
            //生成密钥
            SecretKey deskey = new SecretKeySpec(key.getBytes(),Algorithm);
            //加密,注意这里需要注明分组及补位方式。否则解密时可能导致补位方式不同而报错
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,deskey);
            byte[] decryptBytes = cipher.doFinal(decodeString);
            return new String(decryptBytes);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
}
