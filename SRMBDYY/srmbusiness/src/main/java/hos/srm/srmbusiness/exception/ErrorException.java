package hos.srm.srmbusiness.exception;

public class ErrorException extends RuntimeException{
    private String code;
    private String msg;

    public String getMessage1() {
        return message1;
    }

    public void setMessage1(String message1) {
        this.message1 = message1;
    }

    private  String message1;

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

    public ErrorException(String message) {
        super(message);
        this.msg=message;

    }

    public ErrorException(String code,String message){
        ///setCode(code);
        super(message);
        this.code=code;
        this.msg=message;
    }
    public ErrorException(String code,String message,String message1){
        ///setCode(code);
        super(message);
        this.code=code;
        this.msg=message;
        this.message1=message1;
    }
}