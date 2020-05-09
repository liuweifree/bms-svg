package com.onyouxi.repository.manager;

import com.onyouxi.model.dbModel.UserOrderModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserOrderRepository extends MongoRepository<UserOrderModel,String> {

    UserOrderModel findByUid(String uid);

    UserOrderModel findByOrderId(String orderId);

}
