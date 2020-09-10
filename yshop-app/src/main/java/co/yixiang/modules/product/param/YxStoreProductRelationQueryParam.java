package co.yixiang.modules.product.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 商品点赞和收藏表 查询参数对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-23
 */
@Getter
@Setter
public class YxStoreProductRelationQueryParam  {

    @NotBlank(message = "参数有误")
    private String id;

    private String category;
}
