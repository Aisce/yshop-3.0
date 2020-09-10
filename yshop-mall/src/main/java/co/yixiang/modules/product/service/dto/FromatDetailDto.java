/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.product.service.dto;

import lombok.*;

import java.util.List;

/**
 * @ClassName FromatDetailDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/12
 **/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FromatDetailDto {
    private  String attrHidden;

    private  String detailValue;

    private List<String> detail;

    private String value;

}
