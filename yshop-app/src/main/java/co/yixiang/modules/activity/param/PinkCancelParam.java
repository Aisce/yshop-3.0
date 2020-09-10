package co.yixiang.modules.activity.param;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName PinkCancelParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/6/23
 **/
@Getter
@Setter
public class PinkCancelParam {
    @NotBlank(message = "参数错误")
    private String id;
    @NotBlank(message = "参数错误")
    private String cid;
}
