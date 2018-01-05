package cn.tonghao.remex.common.method;

import cn.tonghao.remex.common.annotation.ResponseJson;
import cn.tonghao.remex.common.annotation.ResponseJson.Location;
import cn.tonghao.remex.common.annotation.ResponseJson.CompressType;
import cn.tonghao.remex.common.util.ClientCompressFactory;
import cn.tonghao.remex.common.util.CompressHttpMessageConverter;
import cn.tonghao.remex.common.util.JsonHttpMessageConverter;
import cn.tonghao.remex.common.util.MessageConverterTpyeUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by howetong on 2018/1/5.
 */
public class ResponseJsonMethodProcessor implements HandlerMethodReturnValueHandler, InitializingBean {
    private HttpMessageConverter messageConverter;
    private List<BeanWrapper> beanWrappers;

    public ResponseJsonMethodProcessor() {
    }

    public List<BeanWrapper> getBeanWrappers() {
        return this.beanWrappers;
    }

    public void setBeanWrappers(List<BeanWrapper> beanWrappers) {
        this.beanWrappers = beanWrappers;
    }

    public HttpMessageConverter getMessageConverter() {
        return this.messageConverter;
    }

    public void setMessageConverter(HttpMessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(ResponseJson.class) != null;
    }

    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        Object result = returnValue;
        mavContainer.setRequestHandled(true);
        ResponseJson responseJson = null;
        ServletServerHttpResponse outputMessage = this.createOutputMessage(webRequest);
        responseJson = (ResponseJson)returnType.getMethodAnnotation(ResponseJson.class);
        HashMap message;
        if(returnValue == null) {
            message = new HashMap();
            message.put("success", Boolean.valueOf(true));
            message.put("data", new HashMap());
            result = message;
        } else if(responseJson.location() != Location.MESSAGE && (!(returnValue instanceof String) || responseJson.location() != Location.UNDEFINED)) {
            Iterator var11 = this.beanWrappers.iterator();

            while(var11.hasNext()) {
                BeanWrapper beanWrapper = (BeanWrapper)var11.next();
                if(beanWrapper.supportsType(returnType)) {
                    result = beanWrapper.wrap(returnValue);
                    break;
                }
            }
        } else {
            message = new HashMap();
            message.put("success", Boolean.valueOf(true));
            message.put("msg", returnValue);
            result = message;
        }

        int converterType;
        if(responseJson.compressType() == CompressType.NOCOMPRESS) {
            if(responseJson.location() == Location.DATA) {
                HttpMessageConverter converter = new JsonHttpMessageConverter();
                converter.write(result, new MediaType(MediaType.APPLICATION_JSON, Collections.singletonMap("charset", "UTF-8")), outputMessage);
            } else if(responseJson.location() == Location.BYREQUEST) {
                converterType = MessageConverterTpyeUtil.getMessageConverterTypeFromHttpHead(webRequest.getHeader("TN-REQ-DATA-TYPE"));
                switch(converterType) {
                    case 2:
                        HttpMessageConverter converter = new JsonHttpMessageConverter();
                        outputMessage.getHeaders().set("TN-RES-DATA-TYPE", "json/text");
                        converter.write(result, new MediaType(MediaType.APPLICATION_JSON, Collections.singletonMap("charset", "UTF-8")), outputMessage);
                        break;
                    case 3:
                        outputMessage.getHeaders().set("TN-RES-DATA-TYPE", "json/base64");
                        this.messageConverter.write(result, new MediaType(MediaType.APPLICATION_JSON, Collections.singletonMap("charset", "UTF-8")), outputMessage);
                        break;
                    default:
                        this.messageConverter.write(result, new MediaType(MediaType.APPLICATION_JSON, Collections.singletonMap("charset", "UTF-8")), outputMessage);
                }
            } else {
                this.messageConverter.write(result, new MediaType(MediaType.APPLICATION_JSON, Collections.singletonMap("charset", "UTF-8")), outputMessage);
            }
        } else {
            int compressType = 0;
            if(responseJson.location() == Location.DATA) {
                converterType = 2;
            } else if(responseJson.location() == Location.BYREQUEST) {
                converterType = MessageConverterTpyeUtil.getMessageConverterTypeFromHttpHead(webRequest.getHeader("TN-REQ-DATA-TYPE"));
                switch(converterType) {
                    case 2:
                        outputMessage.getHeaders().set("TN-RES-DATA-TYPE", "json/text");
                        break;
                    case 3:
                        outputMessage.getHeaders().set("TN-RES-DATA-TYPE", "json/base64");
                        break;
                    default:
                        this.messageConverter.write(result, new MediaType(MediaType.APPLICATION_JSON, Collections.singletonMap("charset", "UTF-8")), outputMessage);
                }
            } else {
                converterType = 3;
            }

            if(responseJson.compressType() == CompressType.GZIP) {
                compressType = 1;
            } else if(responseJson.compressType() == CompressType.SNAPPY) {
                compressType = 2;
            } else if(responseJson.compressType() == CompressType.BYREQUEST) {
                String dataCompressType = webRequest.getHeader("TN-REQ-COMPRESS-TYPE");
                if(null != dataCompressType) {
                    compressType = ClientCompressFactory.getCompressTypeFromHeanderString(dataCompressType);
                }
            }

            CompressHttpMessageConverter compressHttpMessageConverter = new CompressHttpMessageConverter(converterType, compressType);
            compressHttpMessageConverter.write(result, new MediaType(MediaType.APPLICATION_JSON, Collections.singletonMap("charset", "UTF-8")), outputMessage);
        }

    }

    protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
        HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
        return new ServletServerHttpResponse(response);
    }

    public void afterPropertiesSet() throws Exception {
        if(this.beanWrappers == null || this.beanWrappers.size() == 0) {
            throw new Exception("beanWrappers undefined");
        }
    }
}