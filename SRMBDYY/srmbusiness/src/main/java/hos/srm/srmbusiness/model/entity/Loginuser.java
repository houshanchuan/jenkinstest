package hos.srm.srmbusiness.model.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 登录表
 * </p>
 *
 * @author 代码生成器
 * @since 2024-06-22
 */
@Data

public class Loginuser implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String userCode;
    private String userName;
    private String password;
    private String status;
    private String account;
    private String phone;
    private String wxCode;

}
