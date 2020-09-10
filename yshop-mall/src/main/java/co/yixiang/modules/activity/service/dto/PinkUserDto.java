package co.yixiang.modules.activity.service.dto;

import co.yixiang.modules.activity.domain.YxStorePink;
import lombok.*;

import java.util.List;

/**
 * @ClassName PinkUserDto
 * @Author hupeng <610796224@qq.com>
 * @Date 2020/6/22
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PinkUserDto {
    private List<YxStorePink> pinkAll;//拼团的团员
    private YxStorePink pinkT; //单个拼团信息
    private List<Long> idAll; //拼团id集合
    private List<Long> uidAll; //拼团用户id集合
    private Integer count; //还差几人成团
}
