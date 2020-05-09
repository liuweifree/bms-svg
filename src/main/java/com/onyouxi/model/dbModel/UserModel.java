package com.onyouxi.model.dbModel;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class UserModel {

    private String id;

    private Long uid;
    //标示 目前为idfv
    private String sign;

    //用户状态 1正常 2封禁
    private Integer status;

    //订阅结束时间
    private Long payExpires;

    private Long createTime;

    private Long loginTime;

    private Long updateTime;

    //原始订单号  恢复购买使用
    private String originalTransactionId;

    //最后一次的Receipt 用于定时任务校验自动续费
    private String latestReceipt;

    //自动续费的状态 1正常 2退订 3扣费失败
    private Integer payStatus;

    //是否是新用户 按照是否涂满5张图 客户端上报 0 新用户   1老用户
    private Integer newUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getPayExpires() {
        return payExpires;
    }

    public void setPayExpires(Long payExpires) {
        this.payExpires = payExpires;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getOriginalTransactionId() {
        return originalTransactionId;
    }

    public void setOriginalTransactionId(String originalTransactionId) {
        this.originalTransactionId = originalTransactionId;
    }

    public String getLatestReceipt() {
        return latestReceipt;
    }

    public void setLatestReceipt(String latestReceipt) {
        this.latestReceipt = latestReceipt;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getNewUser() {
        return newUser;
    }

    public void setNewUser(Integer newUser) {
        this.newUser = newUser;
    }
}
