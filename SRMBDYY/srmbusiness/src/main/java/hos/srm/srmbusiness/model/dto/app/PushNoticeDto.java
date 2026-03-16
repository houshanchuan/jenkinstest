package hos.srm.srmbusiness.model.dto.app;

import lombok.Data;

import java.io.Serializable;

@Data
public class PushNoticeDto implements Serializable {
    //消息类型  0代表透传消息(使用这个，需要手机自己弹出通知，定义通知样式，content为json串)  1代表是通知(使用这个，标题和内容即手机上显示的通知标题和内容)
    private Integer noticeType;
    //推送用户类型 0 全部用户  1根据cid推送  2根据别名  3根据标签
    private Integer userType;
    //用户标识，可为cid,别名，tag，多个之间逗号隔开
    private String user;
    //推送标题
    private String title;
    //推送内容
    private String content;

}
