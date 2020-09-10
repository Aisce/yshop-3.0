package co.yixiang.modules.product.vo;



import cn.hutool.core.util.StrUtil;
import co.yixiang.modules.product.domain.YxStoreProductAttrValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 商品表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-19
 */
@Data
@ApiModel(value = "YxStoreProductQueryVo对象", description = "商品表查询参数")
public class YxStoreProductQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    private Long id;

    @ApiModelProperty(value = "商户Id(0为总后台管理员创建,不为0的时候是商户后台创建)")
    private Integer merId;

    @ApiModelProperty(value = "商品图片")
    private String image;

    private String image_base;

    private String codeBase;

    public String getImage_base() {
        return image;
    }

    private Boolean userCollect = false;

    private Boolean userLike = false;

    @ApiModelProperty(value = "轮播图")
    private String sliderImage;

    private List<String> sliderImageArr;

    public List<String> getSliderImageArr() {
        //Arrays.asList(sliderImage.split(","));
        if(StrUtil.isNotEmpty(sliderImage)){
            return Arrays.asList(sliderImage.split(","));
        }
        return new ArrayList<>();
    }

    private YxStoreProductAttrValue attrInfo;

    @ApiModelProperty(value = "商品名称")
    private String storeName;

    @ApiModelProperty(value = "商品简介")
    private String storeInfo;

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value = "分类id")
    private String cateId;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "会员价格")
    private BigDecimal vipPrice;

    @ApiModelProperty(value = "市场价")
    private BigDecimal otPrice;

    @ApiModelProperty(value = "邮费")
    private BigDecimal postage;

    @ApiModelProperty(value = "单位名")
    private String unitName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "销量")
    private Integer sales;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "产品描述")
    private String description;

    @ApiModelProperty(value = "是否包邮")
    private Integer isPostage;

    @ApiModelProperty(value = "成本价")
    private BigDecimal cost;

    @ApiModelProperty(value = "秒杀状态 0 未开启 1已开启")
    private Integer isSeckill;

    @ApiModelProperty(value = "砍价状态 0未开启 1开启")
    private Integer isBargain;

    @ApiModelProperty(value = "是否优品推荐")
    private Integer isGood;

    @ApiModelProperty(value = "虚拟销量")
    private Integer ficti;

    @ApiModelProperty(value = "浏览量")
    private Integer browse;

    private Integer isShow;

    private BigDecimal giveIntegral;

    private Integer tempId;

    /** 是否单独分佣 */
    private Integer isSub;

    /** 规格 0单 1多 */
    private Integer specType;


}