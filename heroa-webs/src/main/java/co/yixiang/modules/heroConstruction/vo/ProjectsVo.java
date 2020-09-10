package co.yixiang.modules.heroConstruction.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import co.yixiang.modules.heroConstruction.domain.ConstructionProject;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ProjectsVo {

    private String projectName;


    private String projectCompleted;


    private String projectCate;


    private Integer projectSort;


    private String imgUrl;


    private Long id;


    public void copy(ConstructionProject source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
