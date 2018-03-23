package cn.tonghao.remex.business.pay.util;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class ParameterUtil {

    /**
     * 将Map组装成待签名数据(排除掉sign和sign_type)
     * 待签名的数据必须按照规则顺序排列
     * @param params 原始map
     * @return 按照规则生成的String
     */
    public static String getSignDataIgnoreSignType(Map<String, Object> params) {
        StringBuffer content = new StringBuffer();

        // 按照key做排序
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            if ("sign".equals(key) || "sign_type".equals(key)) {
                continue;
            }
            String value = (String) params.get(key);
            if (value != null && !value.equalsIgnoreCase("null")) {
                content.append((i == 0 ? "" : "&") + key + "=" + value);
            } else {
                content.append((i == 0 ? "" : "&") + key + "=");
            }
        }
        return content.toString();
    }

    /**
     * 将Map组装成待签名数据
     * 待签名的数据必须按照规则顺序排列(排除掉sign和sign_method)
     * @param params 原始map
     * @return 按照规则生成的String
     */
    public static String getSignDataLowerCaseKeyIgnoreNull(Map<String, String> params) {
        StringBuffer content = new StringBuffer();

        // 按照key做排序
        List<String> keys = new ArrayList<String>(params.keySet());
        Comparator<String> comp = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.toLowerCase().compareTo(s2.toLowerCase());
            }
        };
        Collections.sort(keys, comp);
        int count = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            if ("sign".equals(key) ||"sign_method".equals(key)) {
                continue;
            }
            String value = (String) params.get(key);
            if (value != null && !"".equals(value)) {
                content.append((count == 0 ? "" : "&") + key.toLowerCase() + "=" + value);
                count++;
            }
        }
        return content.toString();
    }

    /**
     * 将Map组装成待签名数据
     * 待签名的数据必须按照规则顺序排列(排除掉sign和sign_method)
     * @param params 原始map
     * @return 按照规则生成的String
     */
    public static String getSignDataIgnoreNullAndSignType(Map<String, String> params) {
        StringBuffer content = new StringBuffer();

        // 按照key做排序
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        int count = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            if ("sign".equals(key) ||"sign_type".equals(key)) {
                continue;
            }
            String value = (String) params.get(key);
            if (value != null && !"".equals(value)) {
                content.append((count == 0 ? "" : "&") + key.toLowerCase() + "=" + value);
                count++;
            }
        }
        return content.toString();
    }

    /**
     * 忽略掉Null的值。
     * 待签名的数据必须按照规则顺序排列（排除sign字段）
     * @param params
     * @return
     */
    public static String getSignDataIgnoreNull(Map<String, String> params) {
        StringBuffer content = new StringBuffer();

        // 按照key做排序
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        int count = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if ("sign".equals(key)) {
                continue;
            }
            if (params.get(key) != null) {
                String value = params.get(key);
                if (StringUtils.isNotEmpty(value)) {
                    content.append((count == 0 ? "" : "&") + key + "=" + value);
                    count++;
                }
            }
        }
        return content.toString();
    }

    /**
     * 忽略掉Null的值。
     * 待签名的数据必须按照规则顺序排列（排除sign字段）
     * @param params
     * @return
     */
    public static String getSignDataIgnoreNullObject(Map<String, Object> params) {
        StringBuffer content = new StringBuffer();

        // 按照key做排序
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        int count = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if ("sign".equals(key)) {
                continue;
            }
            if (params.get(key) != null) {
                String value = params.get(key).toString();
                if (StringUtils.isNotEmpty(value)) {
                    content.append((count == 0 ? "" : "&") + key + "=" + value);
                    count++;
                }
            }
        }
        return content.toString();
    }

    /**
     * 忽略掉Null的值。
     * 待签名的数据必须按照规则顺序排列，value值需进过url编码
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getSignDataIgnoreNullWithEncode(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuffer content = new StringBuffer();

        // 按照key做排序
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        int count = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);

            String value = (String) params.get(key);
            if (value != null && !"".equals(value)) {
                content.append((count == 0 ? "" : "&") + key + "=" + URLEncoder.encode(value, "utf-8"));
                count++;
            }
        }
        return content.toString();
    }

    /**
     * 取得URL中的参数值。
     * <p>如不存在，返回空值。</p>
     *
     * @param url
     * @param name
     * @return
     */
    public static String getParameter(String url, String name) {
        if (name == null || name.equals("")) {
            return null;
        }
        name = name + "=";
        int start = url.indexOf(name);
        if (start < 0) {
            return null;
        }
        start += name.length();
        int end = url.indexOf("&", start);
        if (end == -1) {
            end = url.length();
        }
        return url.substring(start, end);
    }

    //得到xml字符串节点内容
    public static String getXmlValue(String xml, String name) {
        if (StringUtils.isBlank(xml) || StringUtils.isBlank(name)) {
            return "";
        }
        int start = xml.indexOf("<" + name + ">");
        start += (name.length() + 2);// 去掉本字符串和"<"、">"的长度
        int end = xml.indexOf("</" + name + ">");
        if (end > start && end <= (xml.length() - name.length() - 2)) {
            return xml.substring(start, end);
        } else {
            return "";
        }
    }

    public static String getXmlValueNull(String xml, String name) {
        if (StringUtils.isBlank(xml) || StringUtils.isBlank(name)) {
            return "";
        }
        int start = xml.indexOf("<" + name + ">");
        start += (name.length() + 2);// 去掉本字符串和"<"、">"的长度
        int end = xml.indexOf("</" + name + ">");
        if (end > start && end <= (xml.length() - name.length() - 2)) {
            return xml.substring(start, end);
        } else {
            return null;
        }
    }

    public static Map<String, String> urlEncode(Map<String, String> param) {
        Map<String, String> p = new HashMap<String, String>();
        try {
            if (param == null || param.isEmpty()) {
                return p;
            }
            for (String key : param.keySet()) {
                p.put(key, encodeURIComponent(param.get(key)));
            }
            return p;
        } catch (Exception e) {
            return p;
        }
    }

    public static String encodeURIComponent(String component) {
        String result = null;
        try {
            result = URLEncoder.encode(component, "UTF-8")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%20", "+")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            result = component;
        }
        return result;
    }
}
