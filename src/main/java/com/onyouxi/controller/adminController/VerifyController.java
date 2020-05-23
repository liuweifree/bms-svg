package com.onyouxi.controller.adminController;

import com.onyouxi.model.pageModel.PageResultModel;
import com.onyouxi.model.pageModel.RestResultModel;
import com.onyouxi.service.VerifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/v1/api/admin/verify")
public class VerifyController {

    @Autowired
    private VerifyService verifyService;

    @RequestMapping(value = "/set", method = RequestMethod.GET)
    public RestResultModel set(Integer verifyStatus){
        RestResultModel restResultModel = new RestResultModel();
        verifyService.setVerifyStatus(verifyStatus);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RestResultModel get(){
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        restResultModel.setData(verifyService.getVerifyStatus());
        return restResultModel;
    }
}
