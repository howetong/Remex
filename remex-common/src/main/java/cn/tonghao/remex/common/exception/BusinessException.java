package cn.tonghao.remex.common.exception;


/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 2526586031514187745L;
    //错误码
    private Integer errorCode;
    //返回错误消息
    private String retMsg;

    public BusinessException(int errorCode, String retMsg) {
        super("[" + errorCode + "] " + retMsg);
        this.retMsg = retMsg;
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCodeDefinition errorCodeDefinition) {
        super("[" + errorCodeDefinition.getErrorCode().getErrorCode() + "] " + errorCodeDefinition.getErrorCode().getErrMessage());
        this.retMsg = errorCodeDefinition.getErrorCode().getErrMessage();
        this.errorCode = errorCodeDefinition.getErrorCode().getErrorCode();
    }

    public BusinessException(ErrorCodeDefinition errorCodeDefinition, String retMsg) {
        this(errorCodeDefinition.getErrorCode().getErrorCode(), retMsg);
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    @Override
    public String toString() {
        return "业务异常{" +
                "errorCode=" + errorCode +
                ", retMsg='" + retMsg + '\'' +
                '}';
    }
}
