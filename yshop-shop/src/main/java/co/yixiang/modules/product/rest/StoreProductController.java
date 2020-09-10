/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.product.rest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.constant.ShopConstants;
import co.yixiang.enums.ShopCommonEnum;
import co.yixiang.enums.SpecTypeEnum;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.aop.ForbidSubmit;
import co.yixiang.modules.category.domain.YxStoreCategory;
import co.yixiang.modules.category.service.YxStoreCategoryService;
import co.yixiang.modules.product.domain.YxStoreProduct;
import co.yixiang.modules.product.domain.YxStoreProductAttrResult;
import co.yixiang.modules.product.service.YxStoreProductAttrResultService;
import co.yixiang.modules.product.service.YxStoreProductReplyService;
import co.yixiang.modules.product.service.YxStoreProductRuleService;
import co.yixiang.modules.product.service.YxStoreProductService;
import co.yixiang.modules.product.service.dto.ProductDto;
import co.yixiang.modules.product.service.dto.ProductFormatDto;
import co.yixiang.modules.product.service.dto.StoreProductDto;
import co.yixiang.modules.product.service.dto.YxStoreProductQueryCriteria;
import co.yixiang.modules.template.domain.YxShippingTemplates;
import co.yixiang.modules.template.service.YxShippingTemplatesService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
* @author hupeng
* @date 2019-10-04
*/
@Api(tags = "商城:商品管理")
@RestController
@RequestMapping("api")
public class StoreProductController {

    private final YxStoreProductService yxStoreProductService;
    private final YxStoreCategoryService yxStoreCategoryService;
    private final YxShippingTemplatesService yxShippingTemplatesService;
    private final YxStoreProductRuleService yxStoreProductRuleService;
    private final YxStoreProductAttrResultService yxStoreProductAttrResultService;

    public StoreProductController(YxStoreProductService yxStoreProductService,
                                  YxStoreCategoryService yxStoreCategoryService,
                                  YxShippingTemplatesService yxShippingTemplatesService,
                                  YxStoreProductRuleService yxStoreProductRuleService,
                                  YxStoreProductAttrResultService yxStoreProductAttrResultService) {
        this.yxStoreProductService = yxStoreProductService;
        this.yxStoreCategoryService = yxStoreCategoryService;
        this.yxShippingTemplatesService = yxShippingTemplatesService;
        this.yxStoreProductRuleService = yxStoreProductRuleService;
        this.yxStoreProductAttrResultService = yxStoreProductAttrResultService;
    }

    @Log("查询商品")
    @ApiOperation(value = "查询商品")
    @GetMapping(value = "/yxStoreProduct")
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_SELECT')")
    public ResponseEntity getYxStoreProducts(YxStoreProductQueryCriteria criteria, Pageable pageable){
        //商品分类
        List<YxStoreCategory> storeCategories = yxStoreCategoryService.lambdaQuery()
                .eq(YxStoreCategory::getIsShow, ShopCommonEnum.SHOW_1.getValue())
                .list();
        List<Map<String,Object>> cateList = new ArrayList<>();
        Map<String, Object> queryAll = yxStoreProductService.queryAll(criteria, pageable);
        queryAll.put("cateList", this.makeCate(storeCategories,cateList,0,1));
        return new ResponseEntity<>(queryAll,HttpStatus.OK);
    }

    @ForbidSubmit
    @Log("新增/修改商品")
    @ApiOperation(value = "新增/修改商品")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PostMapping(value = "/yxStoreProduct/addOrSave")
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_CREATE')")
    public ResponseEntity create(@Validated @RequestBody StoreProductDto storeProductDto){
        yxStoreProductService.insertAndEditYxStoreProduct(storeProductDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @ForbidSubmit
    @Log("删除商品")
    @ApiOperation(value = "删除商品")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY,allEntries = true)
    @DeleteMapping(value = "/yxStoreProduct/{id}")
    @PreAuthorize("hasAnyRole('admin','YXSTOREPRODUCT_ALL','YXSTOREPRODUCT_DELETE')")
    public ResponseEntity delete(@PathVariable Long id){
        yxStoreProductService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }



    @ApiOperation(value = "商品上架/下架")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PostMapping(value = "/yxStoreProduct/onsale/{id}")
    public ResponseEntity onSale(@PathVariable Long id,@RequestBody String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        Integer status = jsonObject.getInteger("status");
        yxStoreProductService.onSale(id,status);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "生成属性")
    @PostMapping(value = "/yxStoreProduct/isFormatAttr/{id}")
    public ResponseEntity isFormatAttr(@PathVariable Long id,@RequestBody String jsonStr){
        return new ResponseEntity<>(yxStoreProductService.getFormatAttr(id,jsonStr),HttpStatus.OK);
    }



    @ApiOperation(value = "获取商品信息")
    @GetMapping(value = "/yxStoreProduct/info/{id}")
    public ResponseEntity info(@PathVariable Long id){
        Map<String,Object> map = new LinkedHashMap<>(3);

        //运费模板
        List<YxShippingTemplates> shippingTemplatesList = yxShippingTemplatesService.list();
        map.put("tempList", shippingTemplatesList);

        //商品分类
        List<YxStoreCategory> storeCategories = yxStoreCategoryService.lambdaQuery()
                .eq(YxStoreCategory::getIsShow, ShopCommonEnum.SHOW_1.getValue())
                .list();

        List<Map<String,Object>> cateList = new ArrayList<>();
        map.put("cateList", this.makeCate(storeCategories,cateList,0,1));

        //商品规格
        map.put("ruleList",yxStoreProductRuleService.list());


        if(id == 0){
            return new ResponseEntity<>(map,HttpStatus.OK);
        }

        //处理商品详情
        YxStoreProduct yxStoreProduct = yxStoreProductService.getById(id);
        ProductDto productDto = new ProductDto();
        BeanUtil.copyProperties(yxStoreProduct,productDto,"sliderImage");
        productDto.setSliderImage(Arrays.asList(yxStoreProduct.getSliderImage().split(",")));
        YxStoreProductAttrResult storeProductAttrResult = yxStoreProductAttrResultService
                .getOne(Wrappers.<YxStoreProductAttrResult>lambdaQuery()
                        .eq(YxStoreProductAttrResult::getProductId,id).last("limit 1"));
        JSONObject result = JSON.parseObject(storeProductAttrResult.getResult());

        if(SpecTypeEnum.TYPE_1.getValue().equals(yxStoreProduct.getSpecType())){
            productDto.setAttr(new ProductFormatDto());
            productDto.setAttrs(result.getObject("value",ArrayList.class));
            productDto.setItems(result.getObject("attr",ArrayList.class));
        }else{
            Map<String,Object> mapAttr = (Map<String,Object>)result.getObject("value",ArrayList.class).get(0);
            ProductFormatDto productFormatDto = ProductFormatDto.builder()
                    .pic(mapAttr.get("pic").toString())
                    .price(Double.valueOf(mapAttr.get("price").toString()))
                    .cost(Double.valueOf(mapAttr.get("cost").toString()))
                    .otPrice(Double.valueOf(mapAttr.get("ot_price").toString()))
                    .stock(Integer.valueOf(mapAttr.get("stock").toString()))
                    .barCode(mapAttr.get("bar_code").toString())
                    .weight(Double.valueOf(mapAttr.get("weight").toString()))
                    .volume(Double.valueOf(mapAttr.get("volume").toString()))
                    .brokerage(Double.valueOf(mapAttr.get("brokerage").toString()))
                    .brokerageTwo(Double.valueOf(mapAttr.get("brokerage_two").toString()))
                    .build();
            productDto.setAttr(productFormatDto);
        }


        map.put("productInfo",productDto);

        return new ResponseEntity<>(map,HttpStatus.OK);
    }


    /**
     *  分类递归
     * @param data 分类列表
     * @param pid 附件id
     * @param level d等级
     * @return list
     */
    private List<Map<String,Object>> makeCate(List<YxStoreCategory> data,List<Map<String,Object>> cateList,int pid, int level)
    {
        String html = "|-----";
        String newHtml = "";

        for (int i = 0; i < data.size(); i++) {
            YxStoreCategory storeCategory = data.get(i);
            int catePid =  storeCategory.getPid();
            Map<String,Object> map = new HashMap<>();
            if(catePid == pid){
                newHtml = String.join("", Collections.nCopies(level,html));
                map.put("value",storeCategory.getId());
                map.put("label",newHtml + storeCategory.getCateName());
                if(storeCategory.getPid() == 0){
                    map.put("disabled",0);
                }else{
                    map.put("disabled",1);
                }
                cateList.add(map);
                data.remove(i);

                i--;
                this.makeCate(data,cateList,storeCategory.getId(),level + 1);
            }
        }


        return cateList;
    }






}
