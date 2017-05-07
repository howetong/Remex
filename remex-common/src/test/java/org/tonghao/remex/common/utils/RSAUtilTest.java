package org.tonghao.remex.common.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.Key;
import java.security.PublicKey;
import java.util.Map;

/**
 * Created by howeTong on 2017/5/7 0007.
 */
public class RSAUtilTest {

    public static void main(String[] args){

        String str = "RSA数字签名算法";
        Map<String,String> map = RSAUtil.generateKeyPair("E://key");
        byte[] publicKey = RSAUtil.getKeyBytes(map.get("publicKey"));
        System.out.println("公钥是："+Base64.encodeBase64String(publicKey));
        byte[] privateKey = RSAUtil.getKeyBytes(map.get("privateKey"));
        System.out.println("私钥是："+Base64.encodeBase64String(privateKey));

        System.out.println("================密钥对构造完毕,甲方将公钥公布给乙方，开始进行加密数据的传输=============");
        System.out.println("原文是："+str);
        byte[] sign = RSAUtil.sign(str,privateKey);
        System.out.println("产生签名："+Base64.encodeBase64String(sign));

        System.out.println("商户使用RSA算法对签名进行验签");
        boolean status = RSAUtil.verify(str,publicKey,sign);
        System.out.println("验签状态："+status+"\n\n");

    }
}
