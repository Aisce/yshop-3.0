package co.yixiang.modules.manage.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName OrderPriceParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/26
 **/
@Data
public class OrderPriceParam implements Serializable {
    @NotBlank(message = "订单编号错误")
    private String orderId;
    @NotBlank(message = "修改价格必填")
    private String price;
}
