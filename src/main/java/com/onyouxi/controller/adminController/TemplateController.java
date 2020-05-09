package com.onyouxi.controller.adminController;

import com.onyouxi.model.dbModel.ImageBgModel;
import com.onyouxi.model.dbModel.TemplateModel;
import com.onyouxi.model.pageModel.PageResultModel;
import com.onyouxi.model.pageModel.RestResultModel;
import com.onyouxi.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/v1/api/admin/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;


    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel findAll(Integer pageNumber, Integer pageSize){
        pageNumber = pageNumber -1;
        Page<TemplateModel> templatePage = templateService.findAll(pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(templatePage.getTotalElements());
        pageResultModel.setRows(templatePage.getContent());
        return pageResultModel;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel save(TemplateModel templateModel){
        RestResultModel restResultModel = new RestResultModel();
        templateService.save(templateModel);
        restResultModel.setResult(200);
        restResultModel.setResult_msg("success");
        return restResultModel;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id){
        RestResultModel restResultModel = new RestResultModel();
        templateService.delete(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RestResultModel list(){
        RestResultModel restResultModel = new RestResultModel();
        List<TemplateModel> templateModelList = templateService.findAll();
        restResultModel.setResult(200);
        restResultModel.setData(templateModelList);
        return restResultModel;
    }
}
