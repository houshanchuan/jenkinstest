package hos.srm.srmbusiness.model.vo;

import lombok.Data;

import java.util.List;
@Data
public class SrmRoleMenuVo {
    private String rowId;
    private String roleId;
    SrmMenuListVo srmMenuListVo;
    private List<Long> menuIdList;
}
