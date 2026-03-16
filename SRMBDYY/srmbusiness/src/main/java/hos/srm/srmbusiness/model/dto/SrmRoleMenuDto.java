package hos.srm.srmbusiness.model.dto;

import lombok.Data;

import java.util.List;
@Data
public class SrmRoleMenuDto {
    private String rowId;
    private String roleId;
    private List<Long> menuIdList;
}
