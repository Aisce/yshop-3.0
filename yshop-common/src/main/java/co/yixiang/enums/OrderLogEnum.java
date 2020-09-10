package co.yixiang.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hupeng
 * 订单操作相关枚举
 */
@Getter
@AllArgsConstructor
public enum OrderLogEnum {

	PINK_ORDER_FAIL_1("ORDER_EXIST","订单生成失败，你已经参加该团了，请先支付订单"),
	PINK_ORDER_FAIL_2("ORDER_EXIST","订单生成失败，你已经在该团内不能再参加了"),
	REFUND_ORDER_SUCCESS("refund_price","退款成功"),
	REMOVE_ORDER("remove_order","删除订单"),
	EVAL_ORDER("order_eval","用户评价"),
	REFUND_ORDER_APPLY("apply_refund","用户申请退款"),
	TAKE_ORDER_DELIVERY("user_take_delivery","用户已收货"),
	PAY_ORDER_FAIL("PAY_DEFICIENCY","余额不足"),
	PAY_ORDER_SUCCESS("pay_success","用户付款成功"),
	CREATE_ORDER_SUCCESS("SUCCESS","订单创建成功"),
	CREATE_ORDER("yshop_create_order","订单生成"),
	NONE_ORDER("NONE","订单OK"),
	EXTEND_ORDER("EXTEND_ORDER","订单已生成");


	private String value;
	private String desc;




}
