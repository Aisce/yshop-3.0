package co.yixiang.modules.order.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName OrderDataVo
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/25
 **/
@Data
public class OrderDataVo implements Serializable {
    private Integer count;
    private Double price;
    private String time;
}
