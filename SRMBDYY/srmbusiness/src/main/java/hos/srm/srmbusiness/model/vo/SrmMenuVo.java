package hos.srm.srmbusiness.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class SrmMenuVo {
    Long rowId;
    String menuId;
    Long parentId;
    String code;
    String path;
    String name;
    String name_zh;
    String name_en;
    String url;
    Integer type;
    String icon;
    Integer orderNum;
    Integer isRouter;
    // 开启标签页显示标志
    Integer isTab;
    Integer iframe;
    // iframe 标签指向的地址（数据以 http 或者 https 开头时，使用 iframe 标签显示）
    String iframeUrl;
    // 开启动态路由标志
    Integer isDynamic;
    Integer allowClose;
    List<SrmMenuVo> subMenuList;
}
