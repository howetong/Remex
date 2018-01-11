package cn.tonghao.remex.common.exception;

/**
 * Created by howetong on 2018/1/11.
 */
public class RestErrorCodeException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1222081685639745419L;
    // 成功标记
    private boolean success = false;
    // 提示信息
    private String message;
    // 错误码
    private int errorCode = 900000;
    // 返回的具体数据
    private Object data;

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }
}