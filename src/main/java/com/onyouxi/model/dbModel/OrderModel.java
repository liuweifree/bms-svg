package com.onyouxi.model.dbModel;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "order")
public class OrderModel {

    private String id;

    //单次支付的订单号
    private String transactionId;

    //原始自动订阅的订单号
    private String originalTransactionId;

    //过期时间
    private Long recordExpiresDate;

    private String productId;

    private Long createTime;

    private Long updateTime;

    //0 等待验证  1验证通过  2验证未通过
    private Integer status;

    private String receipt;

    private String uid;

    private String sign;

    //0 单次付费  1订阅付费 2自动续费
    private Integer type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public Long getRecordExpiresDate() {
        return recordExpiresDate;
    }

    public void setRecordExpiresDate(Long recordExpiresDate) {
        this.recordExpiresDate = recordExpiresDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
