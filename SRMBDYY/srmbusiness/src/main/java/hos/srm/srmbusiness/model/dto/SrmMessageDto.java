package hos.srm.srmbusiness.model.dto;

import lombok.Data;

@Data
public class SrmMessageDto {
    private String userIdStr;
    private String message;
    private String deptId;
    private String code;
}
