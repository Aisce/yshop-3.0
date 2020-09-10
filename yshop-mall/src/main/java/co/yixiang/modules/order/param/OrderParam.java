package co.yixiang.modules.order.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @ClassName OrderParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/28
 **/
@Data
public class OrderParam implements Serializable {
    private String addressId;
    private String bargainId;
    private String combinationId;
    private String couponId;
    private String from;

    @Size(max = 200,message = "长度超过了限制")
    private String mark;
    @NotBlank(message="请选择支付方式")
    private String payType;
    private String phone;
    private String pinkId;
    private String realName;
    private String seckillId;
    private String shippingType;
    private String useIntegral;
    private String isChannel;
    private String storeId;

}
