package cn.tonghao.remex.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by howetong on 2018/1/10.
 */
public class ParameterUtil {

    public static final Logger logger = LoggerFactory.getLogger(ParameterUtil.class);

    /**
     * 生成签名源串，忽略掉Null的值。
     * @param params 参数map
     * @return String 排序的参数串
     */
    public static String getSignDataIgnoreNull(Map<String, String> params) {
        StringBuffer content = new StringBuffer();

        // 按照key做排序
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        int count = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            if ("sign".equals(key)) {
                continue;
            }
            String value = (String) params.get(key);
            if (value != null && !"".equals(value)) {
                content.append((count == 0 ? "" : "&") + key + "=" + value);
                count++;
            }
        }
        return content.toString();
    }

    public static String getJsonRequestData(HttpServletRequest request, HttpServletResponse response) {
        if (null == request) {
            return null;
        }
        String method = request.getMethod();
        String ret = null;
        if (method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("DELETE")) {
            ret = request.getQueryString();
        } else {
            ret = getBodyData(request);//获取请求body
        }
        return ret;
    }

    private static String getBodyData(HttpServletRequest request) {
        StringBuffer data = new StringBuffer();
        String line;
        BufferedReader reader;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine()))
                data.append(line);
        } catch (IOException e) {
            logger.error("get request body data error: ", e);
            return null;
        }
        return data.toString();
    }
}
