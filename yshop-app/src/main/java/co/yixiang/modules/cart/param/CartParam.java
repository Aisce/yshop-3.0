package co.yixiang.modules.cart.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @ClassName 添加购物车CartParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/6/19
 **/
@Getter
@Setter
public class CartParam {
    @Min(value = 1,message = "数量不在合法范围内")
    @Max(value = 9999,message = "数量不在合法范围内")
    private Integer cartNum;
    @JsonProperty(value = "new")
    private Integer isNew = 0;
    @NotNull(message = "参数有误")
    private Long productId;
    //@NotBlank(message = "参数有误")
    private String uniqueId;

    private Long combinationId;
    private Long secKillId;
    private Long bargainId;
}
