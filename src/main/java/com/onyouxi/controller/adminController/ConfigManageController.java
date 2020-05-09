package com.onyouxi.controller.adminController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.onyouxi.model.dbModel.ConfigModel;
import com.onyouxi.model.dbModel.ParamModel;
import com.onyouxi.model.pageModel.RestResultModel;
import com.onyouxi.service.ConfigManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/api/admin/config")
@Slf4j
public class ConfigManageController extends BaseAdminController{

    @Autowired
    private ConfigManageService configManageService;

    @RequestMapping(value = "/type", method = RequestMethod.GET)
    public RestResultModel findByType(Integer type){
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        ConfigModel configModel = configManageService.findByType(type);
        restResultModel.setData(configModel);
        return restResultModel;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResultModel updateByType(String id , String paramList ,Integer type ){
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        ConfigModel configModel = new ConfigModel();
        configModel.setId(id);
        configModel.setType(type);
        configModel.setParamList(JSONObject.parseArray(paramList,ParamModel.class));
        configManageService.updateOrSaveConfig(configModel);
        return restResultModel;
    }


}
