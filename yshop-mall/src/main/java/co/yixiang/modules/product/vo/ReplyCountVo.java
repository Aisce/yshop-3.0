package co.yixiang.modules.product.vo;

import lombok.*;

import java.io.Serializable;

/**
 * @ClassName ReplyCount
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/4
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyCountVo implements Serializable {
    private Integer sumCount;
    private Integer goodCount;
    private Integer inCount;
    private Integer poorCount;
    private String replyChance;
    private String replySstar;

}
