package com.onyouxi.model.view;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Paint {
    private String id;

    private String pngUrl;

    private String zipUrl;

    private List<String> category;

    private String tagKey;
    //展示用的天
    private Long showTime;

    //是否有纹理
    private Integer type;

    //状态
    private Integer status;

    private Long createTime;

}
