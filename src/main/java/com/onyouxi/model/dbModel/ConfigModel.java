package com.onyouxi.model.dbModel;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "config")
public class ConfigModel {

    private String id;

    private List<ParamModel> paramList;

    //0 广告配置  1 商品配置  2 开关
    private Integer type;

}
