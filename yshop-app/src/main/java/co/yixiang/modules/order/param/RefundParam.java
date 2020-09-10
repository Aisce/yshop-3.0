package co.yixiang.modules.order.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName RefundParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/6
 **/
@Data
public class RefundParam implements Serializable {
    @JsonProperty(value = "refund_reason_wap_explain")
    private String refundReasonWapExplain;  //备注
    @JsonProperty(value = "refund_reason_wap_img")
    private String refundReasonWapImg;  //图片
    @NotBlank(message = "请填写退款原因")
    private String text;
    @NotBlank(message = "参数错误")
    private String uni;
}
