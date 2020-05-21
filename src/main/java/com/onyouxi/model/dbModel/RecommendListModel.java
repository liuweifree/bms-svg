package com.onyouxi.model.dbModel;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "recommend_list")
public class RecommendListModel {

    private String id;

    //推荐列表的名称
    private String name;
    //推荐列表的类型 0:推荐   1:每日
    private Integer type;

    //生效的时间
    private Long effectiveTime;

    //状态 0初始状态  1:正常  2:删除
    private Integer status;

    //每日间隔时间
    private Integer intervalTime;

    //新用户还是老用户  0 新用户   1老用户
    private Integer newUser;

    private Long createTime;
    //版本号
    private String version;

    //列表当中的id list
    private List<String> resourceIdList;

}
