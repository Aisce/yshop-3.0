package co.yixiang.modules.activity.vo;


import co.yixiang.modules.activity.vo.YxStoreCombinationQueryVo;
import co.yixiang.modules.activity.vo.YxStorePinkQueryVo;
import co.yixiang.modules.user.vo.YxUserQueryVo;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName PinkInfoVo
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/20
 **/
@Data
@Builder
public class PinkInfoVo implements Serializable {
    private Integer count;
    private String currentPinkOrder;
    private Integer isOk = 0;
    private List<YxStorePinkQueryVo> pinkAll;
    private Integer pinkBool = 0;
    private YxStorePinkQueryVo pinkT;
    private YxStoreCombinationQueryVo storeCombination;
    private String storeCombinationHost;
    private Integer userBool = 0;
    private YxUserQueryVo userInfo;

}
