package co.yixiang.modules.order.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName ComputeVo
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/27
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComputeVo implements Serializable {
    private BigDecimal couponPrice;
    private BigDecimal deductionPrice;
    private BigDecimal payPostage;
    private BigDecimal payPrice;
    private BigDecimal totalPrice;
    private Double usedIntegral; //使用了多少积分
}
