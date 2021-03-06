package co.yixiang.modules.order.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName ProductOrderParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/6/23
 **/
@Getter
@Setter
public class ProductOrderParam {
    @NotBlank(message = "参数有误")
    private String unique;
}
