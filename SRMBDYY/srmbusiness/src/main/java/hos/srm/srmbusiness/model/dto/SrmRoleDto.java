package hos.srm.srmbusiness.model.dto;

import lombok.Data;

@Data
public class SrmRoleDto {
    private Long rowId;
    private String code;
    private String name;
    private String status;
    private int page;
    private int rows;
}
