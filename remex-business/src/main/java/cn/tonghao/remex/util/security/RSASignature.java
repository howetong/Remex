package cn.tonghao.remex.util.security;

import cn.tonghao.remex.log.RemexLogger;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSASignature {

    private static final Logger LOG = RemexLogger.getLogger(RSASignature.class);

    // 商户（RSA）私钥

    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    /**
     * RSA加密
     *
     * @param content    待加密数据
     * @param privateKey 商户私钥
     * @return 密文
     * @throws NoSuchAlgorithmException
     */
    public static String signature(String content, String privateKey) throws Exception {
        return signature(content, privateKey, SIGN_ALGORITHMS);
    }

    /**
     * RSA加密
     *
     * @param content    待加密字符串
     * @param privateKey 私钥
     * @param algorithm  请求算法的标准名称
     * @return 加密字符串结果
     * @throws Exception
     */
    public static String signature(String content, String privateKey, String algorithm) throws Exception {
        String charset = "utf-8";
        PKCS8EncodedKeySpec priPkCS8 = new PKCS8EncodedKeySpec(
                Base64.decode(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyFactory.generatePrivate(priPkCS8);

        Signature signature = Signature
                .getInstance(algorithm);
        signature.initSign(priKey);
        signature.update(content.getBytes(charset));

        byte[] signed = signature.sign();
        return Base64.encode(signed);
    }

    /**
     * RSA加密
     *
     * @param content 待加密数据
     * @param priKey  商户私钥
     * @return 密文
     * @throws NoSuchAlgorithmException
     */
    public static String hexSignature(String content, PrivateKey priKey) throws Exception {
        String charset = "utf-8";
        Signature signature = Signature
                .getInstance(SIGN_ALGORITHMS);
        signature.initSign(priKey);
        signature.update(content.getBytes(charset));

        byte[] signed = signature.sign();
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        HexCode.encode(signed, 0, signed.length, bOut);

        return new String(bOut.toByteArray());
    }

    /**
     * RSAHex加密
     *
     * @param content    待加密数据
     * @param privateKey 商户私钥
     * @return 密文
     * @throws NoSuchAlgorithmException
     */
    public static String RSAHexSignature(String content, String privateKey) throws Exception {
        String charset = "GBK";
        PKCS8EncodedKeySpec priPkCS8 = new PKCS8EncodedKeySpec(
                Base64.decode(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyFactory.generatePrivate(priPkCS8);

        Signature signature = Signature
                .getInstance(SIGN_ALGORITHMS);
        signature.initSign(priKey);
        signature.update(content.getBytes(charset));

        byte[] signed = signature.sign();
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        HexCode.encode(signed, 0, signed.length, bOut);

        return new String(bOut.toByteArray());
    }

    /**
     * RSA验签名检查
     *
     * @param content   待签名数据
     * @param sign      签名值
     * @param publicKey 支付宝公钥
     * @return 布尔值
     */
    public static boolean doCheck(String content, String sign, String publicKey) {
        return doCheck(content, sign, publicKey, SIGN_ALGORITHMS);
    }

    /**
     * RSA验签
     *
     * @param content   待验签字符串
     * @param sign      加密字符串
     * @param publicKey 公钥
     * @param algorithm 请求算法的标准名称
     * @return
     */
    public static boolean doCheck(String content, String sign, String publicKey, String algorithm) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


            Signature signature = Signature.getInstance(algorithm);

            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));

            boolean bverify = signature.verify(Base64.decode(sign));
            return bverify;
        } catch (Exception e) {
            LOG.error("error:", e);
        }

        return false;
    }

    /**
     * RSAHex验签名检查
     *
     * @param content   待签名数据
     * @param sign      签名值
     * @param publicKey 支付宝公钥
     * @return 布尔值
     */
    public static boolean RSAHexDoCheck(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes("GBK"));

            ByteArrayOutputStream bOut = new ByteArrayOutputStream();

            byte[] data = sign.getBytes("GBK");
            HexCode.decode(data, 0, data.length, bOut);

            return signature.verify(bOut.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 支付的加签
     *
     * @param file  私钥文件路径。文件后缀为 .private
     * @param plain 加签前的明文字符串
     * @return 加签后的密文字符串
     */
    public static String sign(File file, String plain) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            // 读取私钥文件
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            PrivateKey pk = (PrivateKey) ois.readObject();
            // 加签
            Signature sgn = Signature.getInstance("DSA");
            sgn.initSign(pk);
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] mdArr = md.digest(plain.getBytes("UTF-8"));
            sgn.update(mdArr);
            byte[] signArr = sgn.sign();
            return SignUtil.byteToHex(signArr);
        } catch (Exception e) {
            LOG.error("CgbPaySignUtil sign error:", e);
        } finally { // 关闭流
            try {
                if (ois != null)
                    ois.close();
            } catch (Exception e) {

            }
            try {
                if (fis != null)
                    fis.close();
            } catch (Exception e) {

            }
        }

        return "1";
    }

    /**
     * 支付的验签
     *
     * @param file  公钥文件路径。文件后缀为 .public
     * @param plain 加签前的明文字符串
     * @param mask  加签后的密文字符串
     * @return
     */
    public static boolean verify(File file, String plain, String mask) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            // 读取公钥文件
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            PublicKey pk = (PublicKey) ois.readObject();
            // 验签
            Signature sgn = Signature.getInstance("DSA");
            sgn.initVerify(pk);
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] mdArr = md.digest(plain.getBytes("UTF-8"));
            sgn.update(mdArr);
            byte[] maskArr = SignUtil.hexToByte(mask);
            return sgn.verify(maskArr);
        } catch (Exception e) {
            LOG.error("CgbPaySignUtil verify error:", e);
        } finally { // 关闭流
            try {
                if (ois != null)
                    ois.close();
            } catch (Exception e) {
            }
            try {
                if (fis != null)
                    fis.close();
            } catch (Exception e) {
            }
        }

        return false;
    }


    public static File getFileByClassPath(String path) {
        Resource resource = new ClassPathResource(path);
        try {
            File file = resource.getFile();
            return file;
        } catch (IOException e) {
            LOG.error("get class path file fail,path:{}", path);
            return null;
        }
    }
}
