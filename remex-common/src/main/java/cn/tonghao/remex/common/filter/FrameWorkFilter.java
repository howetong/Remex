package cn.tonghao.remex.common.filter;

import cn.tonghao.remex.common.exception.BusinessException;
import cn.tonghao.remex.common.exception.RestErrorCodeException;
import cn.tonghao.remex.common.method.FailData;
import cn.tonghao.remex.common.method.impl.ReReadableResponseWrapper;
import cn.tonghao.remex.common.util.Base64JsonHttpMessageConverter;
import com.fasterxml.jackson.core.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by howetong on 2017/7/11.
 */
public class FrameWorkFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(FrameWorkFilter.class);

    //保存本线程sequenceId，标识调用
    private static ThreadLocal<String> sequenceId = new ThreadLocal<String>() {
        public String initialValue() {
            return null;
        }
    };

    //filter忽略列表
    private List<String> noDecodeList = new ArrayList<String>();

    private PathMatcher matcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (ifContainsInNoDecodeList(req.getServletPath())) {
            chain.doFilter(req, resp);
            return;
        }

        try {
            resp.setHeader("Access-Control-Allow-Origin", "*");
            resp.setHeader("Content-Type", "application/json;charset=UTF-8");

            if (req.getHeader("sequence-id") != null) {
                sequenceId.set(req.getHeader("sequence-id")); //将request中的“sequence-id”存入SequenceId变量中
            }
            ReReadableResponseWrapper respw = new ReReadableResponseWrapper(resp);

            //设置成为自定义的wrapper,可以多次获取reader并获取post body
            LogPostInterceptorWrapper logPostInterceptorWrapper = new LogPostInterceptorWrapper(req);

            // pass the request along the filter chain
            chain.doFilter(logPostInterceptorWrapper, respw);
            if (respw.getStatus() == HttpServletResponse.SC_OK) {
                byte[] b = respw.getResponseData();
                ServletOutputStream output = response.getOutputStream();
                output.write(b);
                output.flush();
            }
        } catch (SocketException ex) {
            logger.warn("[FrameWorkFilter] {}", ex.getMessage());
        } catch (Exception ex) {
            Throwable root = ex;
            while (root.getCause() != null) {
                root = root.getCause();
            }
            HttpMessageConverter messageConverter = new Base64JsonHttpMessageConverter();
            logger.error(sequenceId.get(), ex);//对异常信息进行log
            if (root instanceof RestErrorCodeException) {
                FailData failData = new FailData();
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                root.printStackTrace(printWriter);
                failData.setErrorCode(((RestErrorCodeException) root).getErrorCode());
                failData.setData(stringWriter.toString());
                failData.setMsg(root.getMessage() == null ? "系统错误" : root.getLocalizedMessage());
                try {
                    messageConverter.write(failData, MediaType.APPLICATION_JSON, new ServletServerHttpResponse((HttpServletResponse) response));
                } catch (HttpMessageNotWritableException e) {
                    logger.error(sequenceId.get(), e);
                } catch (IOException e) {
                    logger.error(sequenceId.get(), e);
                }
            } else if (root instanceof JsonParseException) {
                FailData failData = new FailData();
                failData.setMsg("参数 Json 格式错误");
                failData.setErrorCode(970001);
                try {
                    messageConverter.write(failData, MediaType.APPLICATION_JSON, new ServletServerHttpResponse((HttpServletResponse) response));
                } catch (HttpMessageNotWritableException e) {
                    logger.error(sequenceId.get(), e);
                } catch (IOException e) {
                    logger.error(sequenceId.get(), e);
                }
            } else {
                throw new RuntimeException(ex);
            }
        } finally {

        }
    }


    /**
     * 判断一个url是否在排除列表内
     * @param servletPath 请求url
     * @return true 在排除列表中； false 不在排除列表中
     */
    private boolean ifContainsInNoDecodeList(String servletPath) {
        boolean ret = false;
        if (noDecodeList.contains(servletPath)) {
            ret = true;
        } else if (!noDecodeList.isEmpty()) {
            for (String filtermapping : noDecodeList) {
                if (matcher.match(filtermapping, servletPath)) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }

    @Override
    public void destroy() {

    }
}
