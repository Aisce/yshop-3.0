package co.yixiang.modules.activity.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName PinkDto
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/19
 **/
@Data
public class PinkDto implements Serializable {
    private Long id;
    private Long uid;
    private Integer people;
    private Double price;
    private Date stopTime;
    private String nickname;
    private String avatar;


    private String count;
    private String h;
    private String i;
    private String s;


}
