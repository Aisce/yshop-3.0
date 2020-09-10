package co.yixiang.modules.order.service.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName PriceGroup
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/27
 **/
@Data
public class PriceGroupDto {

    private BigDecimal costPrice;
    private BigDecimal storeFreePostage;
    private BigDecimal storePostage;
    private BigDecimal totalPrice;
    private BigDecimal vipPrice;

}
