package hos.srm.srmbusiness.exception;

import lombok.Data;

@Data
public class ResultVo {
    private Integer code=0;
    private String msg="";
    private String data="";
    public  ResultVo(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }
    public ResultVo(Integer code,String msg,String data){
        this.code=code;
        this.msg=msg;
        this.data=data;
    }
}
