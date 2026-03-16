package hos.srm.srmbusiness.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
//加上上面注解如果字段为空，返回前台时不显示
public class ReponseResult <T>{
    private  T data;
    private  Integer code;
    private  String msg;

    public  ReponseResult(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }
    public  ReponseResult(Integer code,T data){
        this.code=code;
        this.data=data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public  ReponseResult(Integer code, String msg, T data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }
}
