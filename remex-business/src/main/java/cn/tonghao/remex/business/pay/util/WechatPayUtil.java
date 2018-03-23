package cn.tonghao.remex.business.pay.util;

import cn.tonghao.remex.business.core.log.RemexLogger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.net.URLEncoder;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class WechatPayUtil {

    private static Logger logger = RemexLogger.getLogger(WechatPayUtil.class);

    private static final int LENGTH = 10;

    public static boolean initialized = false;

    /**
     * @return 随机字符串
     */
    public static String genNonceString() {
        return RandomStringUtils.randomAlphanumeric(LENGTH);
    }

    public static Map<String, String> xml2Map(String xmlStr) {
        if (StringUtils.isBlank(xmlStr)) {
            return null;
        }
        DocumentBuilder builder;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            StringReader xmlReader = new StringReader(xmlStr);
            InputSource xmlSource = new InputSource(xmlReader);
            Document document = builder.parse(xmlSource);
            Map<String, String> resultMap = new HashMap<String, String>();
            NodeList nodeList = document.getElementsByTagName("root").item(0).getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                String key = nodeList.item(i).getNodeName();
                if (nodeList.item(i).hasChildNodes()) {
                    String value = nodeList.item(i).getFirstChild().getNodeValue();
                    resultMap.put(key, value);
                }
            }
            return resultMap;
        } catch (Exception e) {
            logger.error("transfer xml to map failed", e);
        }
        return null;
    }

    public static String map2Xml(Map<String, String> map) {
        String resultStr = "<xml>";
        for (Entry<String, String> entry : map.entrySet()) {
            if (!StringUtils.isBlank(entry.getValue())) {
                resultStr += "<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">";
            }
        }
        resultStr += "</xml>";
        return resultStr;
    }

    public static String map2XmlWithNull(Map<String, String> map) {
        String resultStr = "<xml>";
        for (Entry<String, String> entry : map.entrySet()) {
            resultStr += "<" + entry.getKey() + ">" + (StringUtils.isBlank(entry.getValue()) ? "" : entry.getValue())  + "</" + entry.getKey() + ">";
        }
        resultStr += "</xml>";
        return resultStr;
    }

    public static boolean verifySign(Map<String, String> params, String key) {
        return params.get("sign").equals(signMD5(params, key));
    }

    public static String signMD5(Map<String, String> params, String key) {
        String signData = ParameterUtil.getSignDataIgnoreNull(params) + "&key=" + key;
        return DigestUtils.md5Hex(signData).toUpperCase();
    }

    /**
     * 解密
     * @param data 待解密内容
     * @return
     */
    public static String decrypt(byte[] data, String key) throws Exception {
        initialize();
        //生成密钥
        SecretKey deskey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding","BC");
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        return new String(cipher.doFinal(data));
    }

    public static void initialize(){
        if (initialized) return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }


    /**
     * 微信转码将空格转为%20而不是+
     * @param param
     * @return
     */
    public static Map<String, String> urlEncode4WeiXin(Map<String, String> param) {
        Map<String, String> p = new HashMap<String, String>();
        try {
            if (param == null || param.isEmpty()) {
                return p;
            }
            for (String key : param.keySet()) {
                p.put(key, URLEncoder.encode(param.get(key), "UTF-8").replaceAll("\\+", "%20"));
            }
            return p;
        } catch (Exception e) {
            return p;
        }
    }

}
