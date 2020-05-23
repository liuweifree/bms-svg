package com.onyouxi.service;

import org.springframework.stereotype.Service;

@Service
public class VerifyService {

    private Integer verifyStatus=0;

    public void setVerifyStatus(Integer verifyStatus){
        this.verifyStatus = verifyStatus;
    }

    public Integer getVerifyStatus(){
        return verifyStatus;
    }
}
