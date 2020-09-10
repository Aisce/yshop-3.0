package co.yixiang.modules.activity.vo;


import co.yixiang.modules.activity.service.dto.SeckillTimeDto;
import lombok.Data;

import java.util.List;

@Data
public class SeckillConfigVo {


    private List<SeckillTimeDto> seckillTime;

    private String lovely;

    private Integer seckillTimeIndex;
}
