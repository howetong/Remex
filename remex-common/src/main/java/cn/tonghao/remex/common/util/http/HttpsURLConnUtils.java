package cn.tonghao.remex.common.util.http;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * http请求工具类
 * 此工具类提供通过httpsURLConnection方式发送https请求
 * 注意与相同路径下类似功能类HttpClientUtils的区别
 */
public class HttpsURLConnUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpsURLConnUtils.class);

    private static final int TIMEOUT = 10000;
    private static final String ENCODING = "UTF-8";

    public static String executePostUTF(Map<String, String> submitFromData, String requestUrl) {
        try {
            HttpsURLConnection connection = createConnection(requestUrl, ENCODING);
            if (connection == null) {
                return null;
            } else {
                requestServer(connection, getRequestParamString(submitFromData, ENCODING), ENCODING);
                return response(connection, ENCODING);
            }
        } catch (Exception e) {
            logger.error("https请求失败...:", e);
            return null;
        }
    }

    private static void requestServer(HttpsURLConnection connection, String message, String encoding) {
        PrintStream out = null;
        try {
            connection.connect();
            out = new PrintStream(connection.getOutputStream(), false, encoding);
            out.print(message);
            out.flush();
        } catch (Exception e) {
            logger.error("打开https连接失败...:", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private static String response(HttpsURLConnection connection, String encoding) {
        InputStream in = null;
        StringBuilder sb;
        BufferedReader br = null;
        String temp;
        try {
            //如果返回状态码为200,读取返回消息
            if (connection.getResponseCode() == HttpStatus.OK.value()) {
                sb = new StringBuilder();
                in = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(in, encoding));
                while ((temp = br.readLine()) != null) {
                    sb.append(temp);
                }
                return sb.toString();
            }
            return null;
        } catch (Exception e) {
            logger.error("https读取返回数据失败...:", e);
            return null;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (in != null) {
                    in.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e2) {
                logger.error("https连接释放资源失败...:", e2);
            }

        }
    }

    private static HttpsURLConnection createConnection(String requestUrl, String encoding) {
        HttpsURLConnection httpsURLConnection;
        URL url;
        try {
            url = new URL(requestUrl);
            //仅允许https协议使用
            if (!url.getProtocol().equalsIgnoreCase("https")) {
                //非https协议请求返回空
                return null;
            }
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setConnectTimeout(TIMEOUT);
            httpsURLConnection.setReadTimeout(TIMEOUT);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setRequestProperty("Content-type", (new StringBuilder("application/x-www-form-urlencoded;charset=")).append(encoding).toString());
            httpsURLConnection.setRequestMethod("POST");
            return httpsURLConnection;
        } catch (IOException e) {
            logger.error("创建https连接失败...:", e);
            return null;
        }
    }

    private static String getRequestParamString(Map requestParam, String encoding) {
        if (StringUtils.isBlank(encoding)) {
            encoding = "UTF-8";
        }
        StringBuilder sf = new StringBuilder("");
        String reqstr = "";
        if (requestParam != null && requestParam.size() != 0) {
            for (Iterator iterator = requestParam.entrySet().iterator(); iterator.hasNext(); ) {
                Entry en = (Entry) iterator.next();
                try {
                    sf.append((new StringBuilder(String.valueOf(en.getKey()))).append("=").append(en.getValue() != null && !"".equals(en.getValue()) ? URLEncoder.encode((String) en.getValue(), encoding) : "").append("&").toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return "";
                }
            }

            reqstr = sf.substring(0, sf.length() - 1);
        }
        return reqstr;
    }
}
