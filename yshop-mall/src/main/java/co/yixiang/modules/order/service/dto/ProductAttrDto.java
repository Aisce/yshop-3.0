package co.yixiang.modules.order.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ProductAttrDto
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/3
 **/
@Data
public class ProductAttrDto implements Serializable {
    private Long productId;
    private String sku;
    private Double price;
    private String image;
}
