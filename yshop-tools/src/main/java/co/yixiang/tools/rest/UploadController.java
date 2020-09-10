/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.tools.rest;

import cn.hutool.core.util.StrUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.api.YshopException;
import co.yixiang.enums.ShopCommonEnum;
import co.yixiang.tools.domain.QiniuContent;
import co.yixiang.tools.service.LocalStorageService;
import co.yixiang.tools.service.QiNiuService;
import co.yixiang.tools.service.dto.LocalStorageDto;
import co.yixiang.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hupeng
 * @date 2020-01-09
 */
@Api(tags = "上传统一管理")
@RestController
@RequestMapping("/api/upload")
@SuppressWarnings("unchecked")
public class UploadController {

    private final LocalStorageService localStorageService;
    private final QiNiuService qiNiuService;
    private final RedisUtils redisUtils;

    public UploadController(LocalStorageService localStorageService, QiNiuService qiNiuService,
                            RedisUtils redisUtils) {
        this.localStorageService = localStorageService;
        this.qiNiuService = qiNiuService;
        this.redisUtils = redisUtils;
    }


    @ApiOperation("上传文件")
    @PostMapping
    public ResponseEntity<Object> create(@RequestParam(defaultValue = "") String name,
                                         @RequestParam(defaultValue = "") String type,
                                         @RequestParam("file") MultipartFile[] files) {
        String localUrl = redisUtils.getY("admin_api_url");
        if(StrUtil.isBlank(type)){
            localUrl = redisUtils.getY("api_url") + "/api";
        }
        String mode = redisUtils.getY("file_store_mode");
        StringBuilder url = new StringBuilder();
        if (ShopCommonEnum.STORE_MODE_1.getValue().toString().equals(mode)) { //存在走本地
            if(StrUtil.isBlank(localUrl)){
                throw new YshopException("本地上传,请先登陆系统配置后台/移动端API地址");
            }
            for (MultipartFile file : files) {
                LocalStorageDto localStorageDTO = localStorageService.create(name, file);
                if ("".equals(url.toString())) {
                    url = url.append(localUrl + "/file/" + localStorageDTO.getType() + "/" + localStorageDTO.getRealName());
                } else {
                    url = url.append(","+localUrl + "/file/" + localStorageDTO.getType() + "/" + localStorageDTO.getRealName());
                }
            }
        } else {//走七牛云
            for (MultipartFile file : files) {
                QiniuContent qiniuContent = qiNiuService.upload(file, qiNiuService.find());
                if ("".equals(url.toString())) {
                    url = url.append(qiniuContent.getUrl());
                }else{
                    url = url.append(","+qiniuContent.getUrl());
                }
            }
        }

        Map<String, Object> map = new HashMap<>(2);
        map.put("errno", 0);
        map.put("link", url);
        return new ResponseEntity(map, HttpStatus.CREATED);
    }


}
