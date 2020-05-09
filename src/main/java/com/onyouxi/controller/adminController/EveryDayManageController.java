package com.onyouxi.controller.adminController;

import com.onyouxi.model.dbModel.EveryDayModel;
import com.onyouxi.model.dbModel.ResourceModel;
import com.onyouxi.model.pageModel.PageResultModel;
import com.onyouxi.model.pageModel.RestResultModel;
import com.onyouxi.repository.manager.EveryDayRepository;
import com.onyouxi.service.EveryDayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/v1/api/admin/everyday")
public class EveryDayManageController {

    @Autowired
    private EveryDayService everyDayService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel findAll( Integer pageNumber, Integer pageSize){
        pageNumber = pageNumber -1;
        Page<EveryDayModel> everyDayPage = everyDayService.findAll(pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(everyDayPage.getTotalElements());
        pageResultModel.setRows(everyDayPage.getContent());
        return pageResultModel;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel save(EveryDayModel everyDayModel){
        RestResultModel restResultModel = new RestResultModel();
        everyDayService.save(everyDayModel);
        restResultModel.setResult(200);
        restResultModel.setResult_msg("success");
        return restResultModel;
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public RestResultModel del(String id){
        RestResultModel restResultModel = new RestResultModel();
        everyDayService.del(id);
        restResultModel.setResult(200);
        restResultModel.setResult_msg("success");
        return restResultModel;
    }



}
