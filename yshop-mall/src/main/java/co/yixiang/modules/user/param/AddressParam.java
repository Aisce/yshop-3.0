package co.yixiang.modules.user.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @ClassName AddressParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/28
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressParam  implements Serializable {
    private String id;
    @NotBlank
    @Size(min = 1, max = 30,message = "长度超过了限制")
    private String real_name;
    private String post_code;
    private String is_default;
    private String wx_export;
    @NotBlank
    @Size(min = 1, max = 60,message = "长度超过了限制")
    private String detail;
    @NotBlank
    private String phone;
    private AddressDetailParam address;
}
