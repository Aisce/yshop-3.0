package co.yixiang.modules.user.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName AddressDetailParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/28
 **/
@Data
public class AddressDetailParam  implements Serializable {
    @JsonProperty(value = "city_id")
    private Integer cityId;
    private String city;
    private String district;
    private String province;
}
