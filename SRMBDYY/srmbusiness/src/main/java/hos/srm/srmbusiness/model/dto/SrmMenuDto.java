package hos.srm.srmbusiness.model.dto;

import lombok.Data;

@Data
public class SrmMenuDto {
    // 下面是查询条件
    private Long parentId;
    private String parentName;
    private String code;
    private String name;
    private Long rowId;
    private String url;
    private String icon;
    private Integer orderNum;
    private String status;
    private Integer isFrame;
    private Integer isTab;
    private int page;
    private int rows;
}
