/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.heroConstruction.service.dto;

import lombok.Data;
import java.io.Serializable;

/**
* @author Jianzhuo
* @date 2020-09-01
*/
@Data
public class ConstructionProjectDto implements Serializable {

    private String projectName;

    private String projectLocation;

    private String projectBudget;

    private String projectArea;

    private String projectType;

    private String projectCompleted;

    private String projectShortdes;

    private String projectDes;

    private String projectBanner;

    private String projectBrochure;

    private Integer projectOnhome;

    private String projectCate;

    private Integer projectSort;

    private Long id;
}
