package cn.tonghao.remex.util.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加密工具
 * Created by Administrator on 2017/5/7 0007.
 */
public class RSAUtil {

    private static String algorithm = "RSA";

    private static int keySize = 512;

    private static String signAlgorithm = "MD5withRSA";

    private  static  Cipher cipher;

    private static String PUBLIC_KEY = "publicKey";

    private static String PRIVATE_KEY = "privateKey";

    private RSAUtil() {
    }

    /**
     * 加载类时就初始化加密对象
     */
    static{
        try {
            cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用公钥对明文进行加密
     * @param src 要加密的字符串
     * @param publicKeyBytes 公钥数组
     * @return 加密后的字符串
     */
    public static String encrypt(String src,byte[] publicKeyBytes){
        //根据转码后的公钥字符串得到公钥
        PublicKey publicKey = getPublicKey(publicKeyBytes);
        try{
            cipher.init(Cipher.ENCRYPT_MODE,publicKey);
            byte[] encryptBytes = cipher.doFinal(src.getBytes());
            return  new String(encryptBytes);
        }catch(InvalidKeyException e){
            e.printStackTrace();
        }catch(IllegalBlockSizeException e){
            e.printStackTrace();
        }catch (BadPaddingException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用私钥对密文进行解密
     * @param src 要解密的字符串
     * @param privateKeyBytes 私钥数组
     * @return 解密后的字符串
     */
    public static String decrypt(String src,byte[] privateKeyBytes){
        //根据转码后的私钥字符串得到私钥
        PrivateKey privateKey = getPrivateKey(privateKeyBytes);
        try{
            cipher.init(Cipher.DECRYPT_MODE,privateKey);
            byte[] decryptBytes = cipher.doFinal(src.getBytes());
            return new String(decryptBytes);
        }catch(InvalidKeyException e){
            e.printStackTrace();
        }catch(IllegalBlockSizeException e){
            e.printStackTrace();
        }catch (BadPaddingException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用私钥数组对字符串进行签名并返回签名值
     * @param src 要签名的字符串
     * @param
     * @return
     */
    public static byte[] sign(String src,byte[] privateKeyBytes){
        //获得签名数据byte数组
        byte[] data = src.getBytes();
        try{
            PrivateKey privateKey = getPrivateKey(privateKeyBytes);
            //实例化Signature
            Signature signature = Signature.getInstance(signAlgorithm);
            //初始化Signature
            signature.initSign(privateKey);
            //更新
            signature.update(data);
            return  signature.sign();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (InvalidKeyException e){
            e.printStackTrace();
        }catch (SignatureException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 校验数字签名
     * @param src 待校验数据
     * @param
     * @param sign 数字签名
     * @return 校验成功返回true，失败则返回false
     */
    public static boolean verify(String src,byte[] publicKeyBytes,byte[] sign){
        byte[] data = src.getBytes();
        try {
            //获得公钥
            PublicKey publicKey = getPublicKey(publicKeyBytes);
            //实例化Signnature
            Signature signature = Signature.getInstance(signAlgorithm);
            //初始化Signature
            signature.initVerify(publicKey);
            //更新
            signature.update(data);
            //验证
            return signature.verify(sign);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (InvalidKeyException e){
            e.printStackTrace();
        }catch (SignatureException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 生成密钥对，以<密钥名，密钥编码后的字符串值>方式存储
     * @param filePath 生成密钥的路径
     * @return Map<String,String>
     */
    public static Map<String,String> generateKeyPair(String filePath){
        try{
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);
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
            KeyPairGenerator keyPairGenerator=KeyPairGenerator.getInstance(algorithm);
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

    /**
     * 将经过Base64编码后的key字符串转为key的字节数组
     * @param keyString
     * @return
     */
    public static byte[] getKeyBytes(String keyString){
        return  Base64.decodeBase64(keyString.getBytes());
    }

    /**
     * 通过公钥数组得到公钥
     * @param publicKeyBytes 公钥数组
     * @return
     */
    public static PublicKey getPublicKey(byte[] publicKeyBytes){
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (InvalidKeySpecException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据私钥数组得到私钥
     * @param privateKeyBytes 私钥数组
     * @return
     */
    public static PrivateKey getPrivateKey(byte[] privateKeyBytes){
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (InvalidKeySpecException e){
            e.printStackTrace();
        }
        return null;
    }


}
