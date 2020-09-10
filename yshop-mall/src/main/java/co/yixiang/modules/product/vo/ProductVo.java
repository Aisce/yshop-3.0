package co.yixiang.modules.product.vo;

import co.yixiang.common.web.param.QueryParam;
import co.yixiang.modules.product.domain.YxStoreProductAttrValue;
import co.yixiang.modules.product.vo.YxStoreProductAttrQueryVo;
import co.yixiang.modules.product.vo.YxStoreProductQueryVo;
import co.yixiang.modules.product.vo.YxStoreProductReplyQueryVo;
import co.yixiang.modules.product.vo.YxSystemStoreQueryVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品dto
 * </p>
 *
 * @author hupeng
 * @date 2019-10-23
 */
@Data
public class ProductVo{

    private List<YxStoreProductQueryVo> goodList = new ArrayList();


    private Integer merId = 0;

    private String priceName = "";

    private List<YxStoreProductAttrQueryVo> productAttr = new ArrayList();

    private Map<String, YxStoreProductAttrValue>  productValue = new LinkedHashMap<>();

    private YxStoreProductReplyQueryVo reply;

    private String replyChance;

    private Integer replyCount = 0;

    //todo
    private List similarity = new ArrayList();

    private YxStoreProductQueryVo storeInfo;

    private String mapKey;

    //门店
    private YxSystemStoreQueryVo systemStore;

    private Integer uid = 0;

    private String tempName;

}
