package com.onyouxi.model.dbModel;

import lombok.Data;

@Data
public class TransactionModel {

    private String transactionId;

    private String productId;

    //过期时间
    private Long expiresDatMs;

    private String originalTransactionId;
}
