package cn.tonghao.remex.common.core.exception;

/**
 * 错误码
 */
public class ErrorCode {

    private final int errorCode;

    private final String errMessage;


    public ErrorCode(int errorCode, String errMessage) {
        super();
        this.errorCode = errorCode;
        this.errMessage = errMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }


    public String getErrMessage() {
        return errMessage;
    }


    /**
     * (non-Javadoc)
     */
    @Override
    public String toString() {
        return errorCode + "-" + errMessage;
    }
}
