package cn.tonghao.remex.business.pay.enums;

/**
 * Created by howetong on 2018/1/10.
 */
public enum StandardResponseCode {
    SUCCESS(0, "操作成功", true),

    SYS_INVALID_SIGNATURE(990002, "系统异常，非法签名"),
    SYS_INTERNAL_ERROR(990006, "系统内部错误")
    ;

    private int code;
    private String msg;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    StandardResponseCode(int code, String msg, boolean success) {
        this.setCode(code);
        this.setMsg(msg);
        this.setSuccess(success);
    }

    StandardResponseCode(int code, String msg) {
        this.setCode(code);
        this.setMsg(msg);
        this.setSuccess(false);
    }

    public static StandardResponseCode getByCode(int code) {
        for (StandardResponseCode responseCode : StandardResponseCode.values()) {
            if (responseCode.getCode() == code) return responseCode;
        }
        return null;
    }

    public static StandardResponseCode getByMsg(String msg) {
        for (StandardResponseCode responseCode : StandardResponseCode.values()) {
            if (responseCode.getMsg().equals(msg)) return responseCode;
        }
        return null;
    }
}
