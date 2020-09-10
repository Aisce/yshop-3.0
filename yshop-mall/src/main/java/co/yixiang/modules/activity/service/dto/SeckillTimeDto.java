package co.yixiang.modules.activity.service.dto;

import lombok.Data;

@Data
public class SeckillTimeDto {

    private Integer id;
    /**
     * 00:00
     */
    private String time;
    /**
     *状态
     */
    private String state;

    private Integer status;

    private Integer stop;
}
