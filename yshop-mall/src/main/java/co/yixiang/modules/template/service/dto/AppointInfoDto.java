package co.yixiang.modules.template.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName AppointInfoDto
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/5/26
 **/
@Getter
@Setter
public class AppointInfoDto {

    private String a_num;  //包邮件数
    private String a_price; //包邮费用
    private List<RegionDto> place; //包邮地区
    private String placeName;

}
