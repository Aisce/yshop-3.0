package co.yixiang.modules.heroConstruction.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import co.yixiang.modules.heroConstruction.domain.ConstructionProject;
import co.yixiang.modules.heroConstruction.domain.ConstructionProjectmedia;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class ProjectVo {

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


    //project 的媒体集
    private List<ConstructionProjectmedia> projectMedias;


    //project 的type集合
    private List<TypeVo> TypeVos;


    //RELATED PROJECTS
    private List<ProjectsVo> relateProjects;


    public void copy(ConstructionProject source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
