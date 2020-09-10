package co.yixiang.modules.activity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 优惠券发放记录对象StoreCouponUserVO
 * 
 * @author hupeng
 * @date 2020-05-06
 */
@Getter
@Setter
public class StoreCouponUserVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 优惠券发放记录id */
    private Long id;

    /** 优惠券名称 */
    private String couponTitle;

    /** 优惠券的面值 */
    private Double couponPrice;

    /** 最低消费多少金额可用优惠券 */
    private Double useMinPrice;

    /** 优惠券结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date endTime;

    private Integer type;

    private String productId;



}
