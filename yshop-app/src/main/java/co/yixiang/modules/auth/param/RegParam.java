package co.yixiang.modules.auth.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName RegParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/6/25
 **/
@Data
public class RegParam {
    @NotBlank(message = "手机号必填")
    private String account;

    @NotBlank(message = "验证码必填")
    private String captcha;

    @NotBlank(message = "密码必填")
    private String password;

    private String spread;

    private String inviteCode;
}
