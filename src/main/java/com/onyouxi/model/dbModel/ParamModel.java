package com.onyouxi.model.dbModel;

import lombok.Data;

@Data
public class ParamModel {
    //广告或者商品id
    private String pid;
    //广告位置
    private String position;

    //折扣信息
    private String discount;
}
