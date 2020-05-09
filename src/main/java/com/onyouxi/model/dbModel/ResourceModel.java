package com.onyouxi.model.dbModel;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "resource")
public class ResourceModel {

    private String id;

    //列表页使用的图片地址
    private String icon;

    //下载的zip包地址
    private String resourceUrl;
    //svg的地址 用于生成炫酷的icon
    private String svgUrl;

    //多语言的资源名字
    private String name;

    //资源的状态
    private Integer status;

    //标签
    private String tag;

    //所属的分类
    private List<String> category;

    private Long createTime;

    //排序用分数 new:新用户  old:老用户
    private Map<String,Integer> scoreMap;

    //展示用的时间
    private Long showTime;

    //是否有纹理图 0 无纹理 1有纹理
    private Integer type;

    //是否被推荐或者放到每日 0 无  1 有
    private Integer recommend;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getShowTime() {
        return showTime;
    }

    public void setShowTime(Long showTime) {
        this.showTime = showTime;
    }

    public Map<String, Integer> getScoreMap() {
        return scoreMap;
    }

    public void setScoreMap(Map<String, Integer> scoreMap) {
        this.scoreMap = scoreMap;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSvgUrl() {
        return svgUrl;
    }

    public void setSvgUrl(String svgUrl) {
        this.svgUrl = svgUrl;
    }

    public Integer getRecommend() {
        return recommend;
    }

    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }
}
