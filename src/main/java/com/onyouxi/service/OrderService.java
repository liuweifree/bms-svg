package com.onyouxi.service;

import com.onyouxi.model.dbModel.OrderModel;
import com.onyouxi.repository.manager.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;


    public void save(OrderModel orderModel){
        orderRepository.save(orderModel);
    }

    public void updateStatus(String transactionId,Integer status){
        OrderModel orderModel = new OrderModel();
        orderModel.setTransactionId(transactionId);
        orderModel.setStatus(status);
        orderModel.setUpdateTime(System.currentTimeMillis());
        orderRepository.save(orderModel);
    }

    public OrderModel findByTransactionId(String transactionId){
        return orderRepository.findByTransactionId(transactionId);
    }

    public List<OrderModel> findByExpiresDate(Long starTime,Long endTime){
        return orderRepository.findByRecordExpiresDateAndRecordExpiresDateAfter(starTime,endTime);
    }



}
