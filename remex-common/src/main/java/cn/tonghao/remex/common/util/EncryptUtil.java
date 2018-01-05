package cn.tonghao.remex.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * 加密工具 基于DES算法，分组对称加密
 * 有三个参数Key、Data、Mode Data字节数无需是8的倍数
 * 其中Key为8字节共64位
 * <p/>
 */
public class EncryptUtil {

    private static final String key = "remexkey";

    /**
     * Description 对字符串进行DES加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {
        byte[] bt = encrypt(data.getBytes("utf-8"), key.getBytes("utf-8"));
        String encryptResultStr = parseByteToHex(bt);
        return Base64Util.encode(encryptResultStr);
    }

    /**
     * Description 对加密字符串记性DES解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String decrypt(String data) throws Exception {
        String decodeData = Base64Util.decode(data);
        byte[] convertData = parseHexToByte(decodeData);
        byte[] bt = decrypt(convertData, key.getBytes("utf-8"));
        return new String(bt);
    }

    /**
     * Description 使用键值进行加密
     *
     * @param data
     * @param key  加密键
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        Cipher cipher = initCipher(data, key, true);
        return cipher.doFinal(data);
    }

    /**
     * Description 使用键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        Cipher cipher = initCipher(data, key, false);
        return cipher.doFinal(data);
    }


    /**
     * Description 完成密码对象的初始化工作
     *
     * @param data
     * @param key
     * @param switchFlag
     * @return
     * @throws Exception
     */
    private static Cipher initCipher(byte[] data, byte[] key, boolean switchFlag) throws Exception {
        //生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        //从原始秘钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        //创建一个秘钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = secretKeyFactory.generateSecret(dks);

        //Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES");

        //根据switchFlag决定对象模式，用密钥初始化Cipher对象
        cipher.init(switchFlag ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, secretKey, sr);

        return cipher;
    }

    /**
     * Description 将二进制转换成16进制
     *
     * @param buf
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	private static String parseByteToHex(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = StringUtils.join('0', hex);
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * Description 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    private static byte[] parseHexToByte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

}
