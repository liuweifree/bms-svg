package com.onyouxi.task;

import com.onyouxi.model.dbModel.UserModel;
import com.onyouxi.service.OrderService;
import com.onyouxi.service.color.PayService;
import com.onyouxi.service.color.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AppleReceiptTask {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private PayService payService;


    @Scheduled(cron = "0 0/1 * * * ?")
    public void checkReceipt(){
        log.info("checkReceipt start");
        Long startTime = System.currentTimeMillis();
        Long endTime = System.currentTimeMillis()-60*1000;
        List<UserModel> userModelList =  userService.findByPayExpiresBetween(startTime,endTime);
        if(CollectionUtils.isNotEmpty(userModelList)){
            log.info("checkReceipt size:{}",userModelList.size());
            userModelList.forEach(userModel -> {
                payService.checkApplePay(userModel);
            });
        }
        log.info("checkReceipt end");
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkPayFail(){
        log.info("checkPayFail start");
        List<UserModel> userModelList = userService.findByPayStatus(3);
        if(CollectionUtils.isNotEmpty(userModelList)){
            log.info("checkReceipt size:{}",userModelList.size());
            userModelList.forEach(userModel -> {
                payService.checkApplePay(userModel);
            });
        }
        log.info("checkPayFail end");
    }


}
