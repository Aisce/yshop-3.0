package co.yixiang.event;

import lombok.*;

/**
 * @ClassName TemplateBean
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/6/6
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemplateBean {
    private String templateType;
    private String orderId;
    private String time;
    private String price;
    private String deliveryName;
    private String deliveryId;
    private String payType;
    private Long uid;

}
