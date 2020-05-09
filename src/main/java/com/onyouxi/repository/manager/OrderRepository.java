package com.onyouxi.repository.manager;

import com.onyouxi.model.dbModel.OrderModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<OrderModel,String> {

    OrderModel findByTransactionId(String transactionId);

    List<OrderModel> findByRecordExpiresDateAndRecordExpiresDateAfter(Long starTime,Long endTime);

}
