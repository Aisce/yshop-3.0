package co.yixiang.modules.cart.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName CartIds
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/15
 **/
@Data
public class CartIdsParm {
    @NotNull(message = "参数有误")
    List<String> ids;
}
