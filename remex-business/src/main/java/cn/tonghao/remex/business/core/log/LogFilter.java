package cn.tonghao.remex.business.core.log;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 日志UUID初始化Filter
 * Created by howetong on 2017/4/26.
 */
public class LogFilter extends OncePerRequestFilter {
    private static ThreadLocal<String> logTraceId = new ThreadLocal<String>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        genUUID();
        filterChain.doFilter(request, response);
    }

    public static ThreadLocal<String> getUUID() {
        if (null == logTraceId.get()) {
            genUUID();
        }
        return logTraceId;
    }


    private static void genUUID() {
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        logTraceId.set(s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24));
    }
}