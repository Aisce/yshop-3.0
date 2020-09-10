package co.yixiang.modules.heroConstruction.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import co.yixiang.modules.heroConstruction.domain.ConstructionProjectmedia;
import co.yixiang.modules.heroConstruction.domain.ConstructionType;
import co.yixiang.modules.heroConstruction.domain.ConstructionTypemedia;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class TypeVo {

    private String typeName;


    private String bedroomNum;


    private String bathroomNum;


    private String area;


    private String typeDes;


    private Long id;


    //Type 的媒体集
    private List<ConstructionTypemedia> typeMedias;


    public void copy(ConstructionType source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
