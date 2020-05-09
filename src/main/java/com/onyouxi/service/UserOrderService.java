package com.onyouxi.service;

import com.onyouxi.model.dbModel.UserOrderModel;
import com.onyouxi.repository.manager.UserOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserOrderService {

    @Autowired
    private UserOrderRepository userOrderRepository;


    public UserOrderModel findByUid(String uid){
        return userOrderRepository.findByUid(uid);
    }

    public UserOrderModel findByOrderId(String orderId){
        return userOrderRepository.findByOrderId(orderId);
    }

    public void save(UserOrderModel userOrderModel){
        userOrderModel.setCreateTime(System.currentTimeMillis());
        userOrderRepository.save(userOrderModel);
    }

}
