package co.yixiang.modules.order.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName ComputeOrderParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/6/22
 **/
@Getter
@Setter
@ToString
public class ComputeOrderParam {
    //@NotBlank(message = "请选择地址")
    private String addressId;
    private String couponId;
    //@NotBlank(message = "请选择支付方式")
    private String payType;
    private String useIntegral;
    @JsonProperty(value = "shipping_type")
    private String shippingType;
    private String bargainId;
    private String pinkId;
    private String combinationId;
}
