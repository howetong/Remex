package cn.tonghao.remex.common.util;
import cn.tonghao.remex.common.exception.BusinessException;
import cn.tonghao.remex.common.exception.ErrorCodeDefinition;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * 封装返回的VO
 */
public class ResponseVoUtil {

    public static Response success(Object object) {
        Response responseVo = new Response();
        responseVo.setErrorCode(10850000);
        responseVo.setData(object);
        responseVo.setSuccess(true);
        return responseVo;
    }

    public static Response fail(int errorCode, String msg, Object data) {
        return getFailedResponseVo(msg, errorCode, data);
    }

    public static Response fail(int errorCode, String msg) {
        return getFailedResponseVo(msg, errorCode);
    }

    public static Response fail(ErrorCodeDefinition errorCodeDefinition) {
        return getFailedResponseVo(errorCodeDefinition.getErrorCode().getErrMessage(), errorCodeDefinition.getErrorCode().getErrorCode());
    }

    public static Response fail(ErrorCodeDefinition errorCodeDefinition, String msg) {
        return getFailedResponseVo(msg, errorCodeDefinition.getErrorCode().getErrorCode());
    }

    public static Response fail(BusinessException be) {
        return getFailedResponseVo(be.getRetMsg(), be.getErrorCode());
    }

    /**
     * 解析字符串为ResponseVo.
     * 如果字符串为空，或者解析的结果为空，则抛出异常
     * 如果调用返回结果success不为true且data为空，抛出异常
     *
     * @param content 待解析字符串
     * @return Response
     */
    public static Response parseString(String content) {
        if (StringUtils.isBlank(content)) {
            throw new BusinessException(ErrorCodeDefinition.ERROR_PARSE_INVOKE_RESULT, "解析调用返回值出错,返回为空");
        }
        Response vo = JsonUtil.toBean(content, Response.class);
        if (vo == null) {
            throw new BusinessException(ErrorCodeDefinition.ERROR_PARSE_INVOKE_RESULT, "解析调用返回值出错:" + content);
        }
        //success为false，data不为空，不抛异常
        if (!vo.isSuccess()) {
            if (vo.getData() == null) {
                throw new BusinessException(vo.getErrorCode(), vo.getMsg());
            }
            if (vo.getData() instanceof Map) {
                Map data = (Map) vo.getData();
                if (MapUtils.isEmpty(data)) {
                    throw new BusinessException(vo.getErrorCode(), vo.getMsg());
                }
            }
        }
        return vo;
    }

    private static Response getFailedResponseVo(String message, int errorCode, Object data) {
        Response responseVo = new Response();
        responseVo.setSuccess(false);
        responseVo.setMsg(message);
        responseVo.setErrorCode(errorCode);
        responseVo.setData(data);
        return responseVo;
    }

    private static Response getFailedResponseVo(String message, int errorCode) {
        return getFailedResponseVo(message, errorCode, null);
    }

}
