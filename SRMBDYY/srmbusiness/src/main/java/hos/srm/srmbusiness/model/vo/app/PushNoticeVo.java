package hos.srm.srmbusiness.model.vo.app;

import lombok.Data;

import java.io.Serializable;

@Data
public class PushNoticeVo implements Serializable {
    public int code;
    public String msg;
    public Object data;

}
