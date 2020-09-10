/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.product.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* @author hupeng
* @date 2020-05-12
*/

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@TableName("yx_store_product_attr_value")
public class YxStoreProductAttrValue implements Serializable {

    @TableId
    private Long id;


    /** 商品ID */
    private Long productId;


    /** 商品属性索引值 (attr_value|attr_value[|....]) */
    private String sku;


    /** 属性对应的库存 */
    private Integer stock;


    /** 销量 */
    private Integer sales;


    /** 属性金额 */
    private BigDecimal price;


    /** 图片 */
    private String image;


    /** 唯一值 */
     @TableField(value = "`unique`")
    private String unique;


    /** 成本价 */
    private BigDecimal cost;

    /** 商品条码 */
    private String barCode;

    /** 原价 */
    private BigDecimal otPrice;

    /** 重量 */
    private BigDecimal weight;

    /** 体积 */
    private BigDecimal volume;

    /** 一级返佣 */
    private BigDecimal brokerage;

    /** 二级返佣 */
    private BigDecimal brokerageTwo;


    public void copy(YxStoreProductAttrValue source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
