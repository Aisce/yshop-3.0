package co.yixiang.modules.activity.vo;


import co.yixiang.modules.activity.vo.YxStoreSeckillQueryVo;
import co.yixiang.modules.product.vo.YxStoreProductReplyQueryVo;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 秒杀产品表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-12-17
 */
@Data
@Builder
public class StoreSeckillVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private YxStoreProductReplyQueryVo reply;

    private Integer replyCount;

    private YxStoreSeckillQueryVo storeInfo;

    @Builder.Default
    private Boolean userCollect = false;



}