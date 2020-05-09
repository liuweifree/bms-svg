package com.onyouxi.controller.color;


import com.onyouxi.constant.Const;
import com.onyouxi.model.pageModel.RestResultModel;
import com.onyouxi.model.view.Config;
import com.onyouxi.service.color.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/color/config")
@Slf4j
public class ConfigController extends BaseUserController {


    @Autowired
    private ConfigService configService;

    @RequestMapping(value = "/get")
    public RestResultModel getConfig(){
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(Const.SUCCESS);
        Config config = configService.getConfig(getLanguage());
        restResultModel.setData(config);
        return restResultModel;
    }
}
