package com.onyouxi.model.view;

import lombok.Data;

import java.util.List;

@Data
public class EveryDay {
    //当前的资源
    private Paint current;
    //下一个资源
    private Paint next;
    //推荐列表的id
    private String strategyId;
    //时间间隔
    private Integer timeInterval;

}
