package co.yixiang.modules.order.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName PayDto
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/7
 **/
@Data
public class PayParam implements Serializable {
    private String from;
    @NotBlank(message = "选择支付类型")
    private String paytype;
    @NotBlank(message = "参数错误")
    private String uni;
}
