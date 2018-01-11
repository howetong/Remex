package cn.tonghao.remex.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by howetong on 2018/1/10.
 */
public class ParameterUtil {

    /**
     * 生成签名源串，忽略掉Null的值。
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
}
