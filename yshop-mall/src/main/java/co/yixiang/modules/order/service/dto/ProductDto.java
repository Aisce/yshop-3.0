package co.yixiang.modules.order.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ProductVo
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/3
 **/
@Data
public class ProductDto implements Serializable {
    private String image;
    private Double price;
    private String storeName;
    private ProductAttrDto attrInfo;

}
