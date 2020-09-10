/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.api;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * API 返回结果
 * @author hupeng
 * @date 2020-04-30
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
public class ApiResult<T> implements Serializable {
    private static final long serialVersionUID = 8004487252556526569L;

    /**
     * 响应码
     */
    private int status;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date time;

    public ApiResult() {
        time  = new Date();
    }

    public static ApiResult<Boolean> result(boolean flag){
        if (flag){
            return ok();
        }
        return fail();
    }

    public static ApiResult<Boolean> result(ApiCode apiCode){
        return result(apiCode,null);
    }

    public static <T> ApiResult<T> result(ApiCode apiCode, T data){
        return result(apiCode,null,data);
    }

    public static <T> ApiResult<T> result(ApiCode apiCode, String message, T data){
        boolean success = false;
        if (apiCode.getCode() == ApiCode.SUCCESS.getCode()){
            success = true;
        }
        if (StringUtils.isBlank(message)){
            message = apiCode.getMessage();
        }
        return (ApiResult<T>) ApiResult.builder()
                .status(apiCode.getCode())
                .msg(message)
                .data(data)
                .success(success)
                .time(new Date())
                .build();
    }

    public static ApiResult<Boolean> ok(){
        return ok(null);
    }

    public static <T> ApiResult<T> ok(T data){
        return result(ApiCode.SUCCESS,data);
    }

    public static <T> ApiResult<T> ok(T data, String message){
        return result(ApiCode.SUCCESS,message,data);
    }

    public static ApiResult<Map<String,Object>> okMap(String key, Object value){
        Map<String,Object> map = new HashMap<>(1);
        map.put(key,value);
        return ok(map);
    }

    public static ApiResult<Boolean> fail(ApiCode apiCode){
        return result(apiCode,null);
    }

    public static ApiResult<String> fail(String message){
        return result(ApiCode.FAIL,message,null);

    }

    public static <T> ApiResult<T> fail(ApiCode apiCode, T data){
        if (ApiCode.SUCCESS == apiCode){
            throw new RuntimeException("失败结果状态码不能为" + ApiCode.SUCCESS.getCode());
        }
        return result(apiCode,data);

    }

    public static ApiResult<String> fail(Integer errorCode, String message){
        return new ApiResult<String>()
                .setSuccess(false)
                .setStatus(errorCode)
                .setMsg(message);
    }

    public static ApiResult<Map<String,Object>> fail(String key, Object value){
        Map<String,Object> map = new HashMap<>(1);
        map.put(key,value);
        return result(ApiCode.FAIL,map);
    }

    public static ApiResult<Boolean> fail() {
        return fail(ApiCode.FAIL);
    }
}