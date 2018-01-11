package cn.tonghao.remex.common.method.impl;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * 对Response进行包装以便于在返回时对response的data进行log
 * */
public class ReReadableResponseWrapper extends HttpServletResponseWrapper {
    /**
     * Indicate that getWriter() is already called.
     */
    public static final int OUTPUT_WRITER = 1;

    /**
     * Indicate that getOutputStream() is already called.
     */
    public static final int OUTPUT_STREAM = 2;


    private int status = SC_OK;
    private ServletOutputStream output = null;
    private PrintWriter writer = null;
    private ByteArrayOutputStream buffer = null;

    public ReReadableResponseWrapper(HttpServletResponse resp) throws IOException {
        super(resp);
        buffer = new ByteArrayOutputStream();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        super.setStatus(status);
        this.status = status;
    }

    public void setStatus(int status, String string) {
        super.setStatus(status, string);
        this.status = status;
    }

    public void sendError(int status, String string) throws IOException {
        super.sendError(status, string);
        this.status = status;
    }

    public void sendError(int status) throws IOException {
        super.sendError(status);
        this.status = status;
    }

    public void sendRedirect(String location) throws IOException {
        super.sendRedirect(location);
        this.status = SC_MOVED_TEMPORARILY;
    }

    public PrintWriter getWriter() throws IOException {
        if (null == writer) {
            writer = new PrintWriter(getOutputStream());
        }
        return writer;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (null == output) {
            output = new WrappedOutputStream(buffer);
        }
        return output;
    }

    public void flushBuffer() throws IOException {
        if (null != writer) {
            writer.flush();
        } else if (null != output) {
            output.flush();
        }
    }

    public void reset() {
        buffer.reset();
    }

    /**
     * Call this method to get cached response data.
     *
     * @return byte array buffer.
     * @throws IOException
     */
    public byte[] getResponseData() throws IOException {
        flushBuffer();
        return buffer.toByteArray();
    }

    /**
     * This class is used to wrap a ServletOutputStream and
     * store output stream in byte[] buffer.
     */
    class WrappedOutputStream extends ServletOutputStream {

        private ByteArrayOutputStream buffer;

        public WrappedOutputStream(ByteArrayOutputStream buffer) {
            this.buffer = buffer;
        }

        public void write(int b) throws IOException {
            buffer.write(b);
        }

        public byte[] toByteArray() {
            return buffer.toByteArray();
        }
    }
}
