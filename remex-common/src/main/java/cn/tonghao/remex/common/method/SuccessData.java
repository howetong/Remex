package cn.tonghao.remex.common.method;

/**
 * Created by howetong on 2018/1/5.
 */
public class SuccessData implements ResponseData {
    private boolean success = true;
    private Object data;

    public SuccessData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
