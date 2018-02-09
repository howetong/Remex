package cn.tonghao.remex.common.util.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加密工具
 * Created by Administrator on 2017/5/7 0007.
 */
public class RSAUtils {

    /**
     * 加密算法
     */
    private static String KEY_ALGORITHM = "RSA";

    private static int keySize = 1024;

    /**
     * 签名算法
     */
    private static String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static String PUBLIC_KEY = "publicKey";

    private static String PRIVATE_KEY = "privateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static String CHARSET = "UTF-8";

    private RSAUtils() {
    }

    /**
     * 使用公钥对明文进行加密
     * @param src 要加密的字符串
     * @param publicKeyStr 公钥字符串
     * @return 加密后的字符串
     */
    public static String encryptByPublicKey(String src, String publicKeyStr, String charSet){
        if(StringUtils.isBlank(charSet)) charSet = CHARSET;
        try{
            //根据转码后的公钥字符串得到公钥
            byte[] keyBytes = Base64.decodeBase64(publicKeyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            //对数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] data = src.getBytes(charSet);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;//下标
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return Base64.encodeBase64String(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用私钥对密文进行解密
     * @param src 要解密的字符串
     * @param privateKeyStr 私钥字符串
     * @return 解密后的字符串
     */
    public static String decryptByPrivateKey(String src, String privateKeyStr, String charSet){
        if(StringUtils.isBlank(charSet)) charSet = CHARSET;
        try{
            byte[] keyBytes = Base64.decodeBase64(privateKeyStr);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateK);
            byte[] data = src.getBytes(charSet);
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return Base64.encodeBase64String(encryptedData);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用私钥对字符串进行签名并返回签名值
     * @param src 要签名的字符串
     * @param
     * @return
     */
    public static String sign(String src, String privateKey, String charSet){
        if (StringUtils.isBlank(charSet)) charSet = CHARSET;
        try{
            byte[] keyBytes = Base64.decodeBase64(privateKey);
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateK);
            signature.update(src.getBytes(charSet));
            byte[] result = signature.sign();
            return Base64.encodeBase64String(result);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 校验数字签名
     * @param src 待校验数据
     * @param sign 数字签名
     * @param publicKeyStr 公钥字符串
     * @return 校验成功返回true，失败则返回false
     */
    public static boolean verify(String src, String sign, String publicKeyStr, String charSet){
        if (StringUtils.isBlank(charSet)) charSet = CHARSET;
        try {
            byte[] keyBytes = Base64.decodeBase64(publicKeyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicK = keyFactory.generatePublic(keySpec);

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicK);
            signature.update(src.getBytes(charSet));
            return signature.verify(Base64.decodeBase64(sign));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 生成密钥对，以<密钥名，密钥编码后的字符串值>方式存储
     * @param filePath 生成密钥的路径
     * @return Map<String,String>
     */
    public static Map<String,String> generateKeyPair(String filePath){
        try{
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            //密钥位数
            keyPairGen.initialize(keySize);
            //生成密钥对
            KeyPair keyPair = keyPairGen.generateKeyPair();
            //获得公钥
            PublicKey publicKey = keyPair.getPublic();
            //获得私钥
            PrivateKey privateKey = keyPair.getPrivate();
            //得到公钥字符串
            String publicKeyString = getKeyString(publicKey);
            //得到私钥字符串
            String privateKeyString = getKeyString(privateKey);
            File path = new File(filePath);
            if (!path.exists()){
                path.mkdir();
            }
            //将密钥对存储到密钥文件中
            BufferedWriter pubbw = new BufferedWriter(new FileWriter(filePath+"/publicKey.keystore"));
            BufferedWriter pribw = new BufferedWriter(new FileWriter(filePath+"/privateKey.keystore"));
            pubbw.write(publicKeyString);
            pribw.write(privateKeyString);
            pubbw.flush();
            pribw.flush();
            pubbw.close();
            pribw.close();
            //将生成的密钥对返回
            Map<String,String> map = new HashMap<String, String>();
            map.put(PUBLIC_KEY,publicKeyString);
            map.put(PRIVATE_KEY,privateKeyString);
            return map;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化密钥对并返回，以<密钥名，密钥对象>方式存储
     * @return Map 密钥的Map
     * */
    public static Map<String,Object> generateKeyPair() {
        try{
            //实例化密钥生成器
            KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance(KEY_ALGORITHM);
            //初始化密钥生成器
            keyPairGenerator.initialize(keySize);
            //生成密钥对
            KeyPair keyPair=keyPairGenerator.generateKeyPair();
            //甲方公钥
            PublicKey publicKey=keyPair.getPublic();
            //甲方私钥
            PrivateKey privateKey= keyPair.getPrivate();
            //将密钥存储在map中
            Map<String,Object> keyMap=new HashMap<String,Object>();
            keyMap.put(PUBLIC_KEY, publicKey);
            keyMap.put(PRIVATE_KEY, privateKey);
            return keyMap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过密钥得到密钥字符串，并进行Base64转码
     * @param key 密钥对象
     * @return
     * @throws Exception
     */
    public static String getKeyString(Key key){
        //将key转为字节数组
        byte[] keyBytes = key.getEncoded();
        //对key字节数组进行Base64转码,得到key字符串
        return Base64.encodeBase64String(keyBytes);
    }

}
