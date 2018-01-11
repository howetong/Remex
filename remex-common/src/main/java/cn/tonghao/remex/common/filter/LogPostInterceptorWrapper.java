package cn.tonghao.remex.common.filter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogPostInterceptorWrapper extends HttpServletRequestWrapper {

    //原始请求实体
    private HttpServletRequest original;
    //实际数据
    private byte[] reqBytes;
    //首次调用getReader标志
    private boolean firstTime = true;

    /**
     * @param request
     */
    public LogPostInterceptorWrapper(HttpServletRequest request) {
        super(request);
        original = request;
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    public BufferedReader getReader() throws IOException {

        if (firstTime)
            firstTime();

        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(reqBytes), "UTF-8");
        return new BufferedReader(isr);
    }

    /**
     * @return
     * @throws IOException
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {

        if (firstTime)
            firstTime();

        ServletInputStream sis = new ServletInputStream() {
            private int i;

            @Override
            public int read() throws IOException {
                byte b;
                if (reqBytes.length > i)
                    b = reqBytes[i++];
                else
                    b = -1;

                return b;
            }
        };

        return sis;
    }

    /**
     * @throws IOException
     */
    private void firstTime() throws IOException {
        firstTime = false;
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = original.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        reqBytes = buffer.toString().getBytes("UTF-8");
    }
}
