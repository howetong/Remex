package cn.tonghao.remex.business.core.util;

import cn.tonghao.remex.business.pay.enums.StandardResponseCode;
import cn.tonghao.remex.common.util.Response;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by howetong on 2018/1/10.
 */
public class ResponseUtil {

    public static Response genResponse(StandardResponseCode standardResponseCode) {
        return genResponse(standardResponseCode, null);
    }

    public static Response genResponse(StandardResponseCode standardResponseCode, Object data) {
        Response vo = new Response();
        vo.setSuccess(standardResponseCode.isSuccess());
        vo.setErrorCode(standardResponseCode.getCode());
        vo.setMsg(standardResponseCode.getMsg());
        vo.setData(data);
        return vo;
    }

}
