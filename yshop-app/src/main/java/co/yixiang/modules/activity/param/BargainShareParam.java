package co.yixiang.modules.activity.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName BargainShareParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/6/20
 **/
@Getter
@Setter
public class BargainShareParam {
    @NotBlank(message = "参数有误")
    private String bargainId;

    private String from;
}
