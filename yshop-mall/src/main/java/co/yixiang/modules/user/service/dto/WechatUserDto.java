package co.yixiang.modules.user.service.dto;

import lombok.*;

/**
 * @ClassName WechatUserDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/6/4
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WechatUserDto {

    private String openid;

    private String unionId;

    private String routineOpenid;

    private String nickname;

    private String headimgurl;

    private Integer sex;

    private String city;

    private String language;

    private String province;

    private String country;

    private Boolean subscribe;

    private Long subscribeTime;

}
