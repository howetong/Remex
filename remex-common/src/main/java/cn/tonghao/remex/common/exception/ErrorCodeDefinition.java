package cn.tonghao.remex.common.exception;

/**
 * 错误码接口
 * 1800001~1809999 为公共错误信息
 */
public enum ErrorCodeDefinition {
    INVALID_PARAMETER(new ErrorCode(10850001, "参数不合法")),
    ERROR_SYSTEM(new ErrorCode(10850002, "系统异常")), // 系统异常
    ERROR_HTTP(new ErrorCode(10850003, "REST接口异常")),
    ERROR_PARSE_INVOKE_RESULT(new ErrorCode(10850005, "解析调用结果异常")),
    INVALID_DATA(new ErrorCode(10850006, "数据异常")),
    NULL_DATA(new ErrorCode(10850007, "空数据异常")),
    PARAMETER_NULL(new ErrorCode(10850008, "参数为NULL")),
    ERROR_IP_NOT_ALLOWED(new ErrorCode(108500010, "IP不允许访问此接口")),
    ERROR_PERMISSIONS_DENIED(new ErrorCode(108500011, "没有权限访问")),
    ERROR_DOWNLOAD_FILE_FAIL(new ErrorCode(108500012, "下载文件失败")),
    ERROR_NO_NOTIFICATION_HANDLER(new ErrorCode(10850012, "没有通知处理的实现类")),
    ERROR_MQ(new ErrorCode(10850013, "向队列发送消息失败")),
    ERROR_SIGN(new ErrorCode(10850014, "签名不正确")),
    ;


    /**
     * 错误码实例
     */
    private ErrorCode errorCode;

    ErrorCodeDefinition(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 设置静态异常错误描述
     *
     * @param errMessage 错误描述字符串：系统异常
     * @return 异常码错误对象
     */
    public ErrorCode setMessage(String errMessage) {
        return new ErrorCode(errorCode.getErrorCode(), errMessage);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }


}
