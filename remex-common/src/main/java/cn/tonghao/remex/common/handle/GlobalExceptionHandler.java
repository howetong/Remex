package cn.tonghao.remex.common.handle;


import cn.tonghao.remex.common.constants.FrameworkConstants;
import cn.tonghao.remex.common.exception.BusinessException;
import cn.tonghao.remex.common.exception.ErrorCodeDefinition;
import cn.tonghao.remex.common.util.HideSensitiveUtil;
import cn.tonghao.remex.common.util.JsonUtil;
import cn.tonghao.remex.common.util.Response;
import cn.tonghao.remex.common.util.ResponseVoUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 处理controller抛出的异常
 */
public class GlobalExceptionHandler extends ExceptionHandlerExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request,
                                                           HttpServletResponse response,
                                                           HandlerMethod handlerMethod,
                                                           Exception exception) {

        String controllerName = handlerMethod.getBean().getClass().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();
        String param = null;
        try {
            param = IOUtils.toString(request.getReader());
        } catch (IOException e) {
            //ignore, try other method
            param = JsonUtil.toString(request.getParameterMap());
        }
        logger.info("调用出错, Controller:{}.{}, 参数:{}", controllerName, methodName, HideSensitiveUtil.hideSensitiveInfo(param));

        if (exception instanceof BusinessException) {
            BusinessException be = (BusinessException) exception;
            logger.info("异常信息,code:{}, message:{}", be.getErrorCode(), be.getRetMsg());
            Response responseVo = ResponseVoUtil.fail(be.getErrorCode(), be.getRetMsg());
            sendResponseData(JsonUtil.toString(responseVo), HttpServletResponse.SC_OK, response);
        } else {
            logger.error("异常信息", exception);
            Response responseVo = ResponseVoUtil.fail(ErrorCodeDefinition.ERROR_SYSTEM);
            sendResponseData(JsonUtil.toString(responseVo), HttpServletResponse.SC_OK, response);
        }
        return new ModelAndView();
    }

    private void sendResponseData(String jsonData, int responseStatus, HttpServletResponse response) {
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.setStatus(responseStatus);
            PrintWriter writer = response.getWriter();
            writer.write(Base64.encodeBase64String(jsonData.getBytes(FrameworkConstants.CHARSET)));
            response.flushBuffer();
        } catch (IOException e) {
            logger.error("处理异常信息时发生错误", e);
        }

    }
}
