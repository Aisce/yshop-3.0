package co.yixiang.modules.user.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName RechargeParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/12/8
 **/
@Data
public class RechargeParam  implements Serializable {

    @NotBlank(message = "充值参数有误")
    @JsonProperty(value = "rechar_id")
    private String recharId;

    private String from;

    //@NotNull(message = "金额必填")
   // @Min(value = 1,message = "充值金额不能低于1")
    private Double price;

    @JsonProperty(value = "paid_price")
    private Double paidPrice;

    private String orderSn;
}
