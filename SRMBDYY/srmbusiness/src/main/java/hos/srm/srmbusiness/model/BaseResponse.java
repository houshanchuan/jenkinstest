package hos.srm.srmbusiness.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BaseResponse<T> {
    private String code;
    private String msg;



    private T data;
    public BaseResponse() {
    }

    public BaseResponse(String code, String msg, T data) {
        this.setCode(code);
        this.setMsg(msg);
        this.setData(data);
    }

    public static <T> BaseResponse<T> error(String code, String message) {
        return new BaseResponse(code, message, (Object)null);
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
