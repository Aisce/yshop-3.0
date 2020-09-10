package co.yixiang.modules.order.vo;

import co.yixiang.modules.order.service.dto.ProductDto;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 订单商品对象
 * </p>
 *
 * @author hupeng
 * @date 2019-11-03
 */
@Data
@Builder
public class OrderCartInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;


    private String orderId;


    private Long productId;


    private Integer cartNum;


    private Long combinationId;

    private Long seckillId;

    private Long bargainId;

    private ProductDto productInfo;




}