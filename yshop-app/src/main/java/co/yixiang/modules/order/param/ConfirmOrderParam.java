package co.yixiang.modules.order.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName 确认订单ConfirmOrderDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/6/21
 **/
@Getter
@Setter
public class ConfirmOrderParam {
    @NotBlank(message = "请提交购买的商品")
    private String cartId;
}
