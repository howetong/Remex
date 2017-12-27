package cn.tonghao.remex.util.security;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class SignUtil {

    private SignUtil(){
    }

    /**
     * @param data     被签名的原数据字节数组，xml去掉signData节点。
     * @param signData 签名字节数组。
     * @param certFile X.509标准的证书文件。
     * @return 如果验签通过，就返回true
     * @throws RuntimeException 运行时异常
     */
    public static boolean veriSign(byte[] data, byte[] signData, String certFile)
            throws RuntimeException {

        InputStream is = null;
        try {
            //加载公钥
            is = new FileInputStream(certFile);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(is);

            PublicKey publicKey = cert.getPublicKey();

            Signature sig = Signature.getInstance("SHA1WithRSA");
            byte[] signed = Base64Binrary.decodeBase64Binrary(new String(signData));
            sig.initVerify(publicKey);
            sig.update(data);
            return sig.verify(signed);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }

            }
        }
    }

    /**
     * @param tr3Xml   tr3的xml。
     * @param certFile X.509标准的证书文件。
     * @return 如果验签通过就返回true
     * @throws RuntimeException
     */

    public static boolean veriSignForXml(String tr3Xml, String certFile)
            throws RuntimeException {
        String dataBeforeSign = tr3Xml.replaceAll("<signature>.*</signature>", "");
        System.out.println(dataBeforeSign);
        int beginIndex = tr3Xml.indexOf("<signature>");
        int endIndex = tr3Xml.indexOf("</signature>");
        String signData = tr3Xml.substring(beginIndex + 11, endIndex);
        System.out.println(signData);

        try {
            return veriSign(dataBeforeSign.getBytes("UTF-8"),
                    signData.getBytes("UTF-8"), certFile);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }


    /**
     * 将十六进制字符串转换成二进制数组
     */
    public static byte[] hexToByte(String hex) {
        byte[] ret = null;
        byte[] tmp = hex.getBytes();
        int length = tmp.length / 2;
        ret = new byte[length];
        for (int i = 0; i < length; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public static String byteToHex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    /**
     * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
     *
     * @param src0 byte
     * @param src1 byte
     * @return byte
     */
    private static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

}
