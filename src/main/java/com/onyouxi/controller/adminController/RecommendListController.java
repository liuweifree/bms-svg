package com.onyouxi.controller.adminController;

import com.alibaba.fastjson.JSON;
import com.onyouxi.model.dbModel.RecommendListModel;
import com.onyouxi.model.pageModel.PageResultModel;
import com.onyouxi.model.pageModel.RestResultModel;
import com.onyouxi.service.RecommendListService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/v1/api/admin/recommendlist")
public class RecommendListController {

    @Autowired
    private RecommendListService recommendListService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel findAll(Integer pageNumber, Integer pageSize){
        pageNumber = pageNumber -1;
        Page<RecommendListModel> ImageBgPage = recommendListService.findAll(pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(ImageBgPage.getTotalElements());
        pageResultModel.setRows(ImageBgPage.getContent());
        return pageResultModel;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel save(RecommendListModel recommendListModel){
        RestResultModel restResultModel = new RestResultModel();
        RecommendListModel rlm = recommendListService.findByEffectiveTime(recommendListModel.getEffectiveTime());
        if( null != rlm){
            restResultModel.setResult(400);
            restResultModel.setResult_msg("存在相同相同的日期,请修改");
            return restResultModel;
        }
        if(!StringUtils.isEmpty(recommendListModel.getVersion())) {
            rlm = recommendListService.findByVersion(recommendListModel.getVersion());
            if( null != rlm){
                restResultModel.setResult(401);
                restResultModel.setResult_msg("存在相同相同的版本号,请修改");
                return restResultModel;
            }
        }
        recommendListService.save(recommendListModel);
        restResultModel.setResult(200);
        restResultModel.setResult_msg("success");
        return restResultModel;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id){
        RestResultModel restResultModel = new RestResultModel();
        recommendListService.delete(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResultModel update(RecommendListModel recommendListModel){
        log.info("update:{}", JSON.toJSONString(recommendListModel));
        RestResultModel restResultModel = new RestResultModel();
        if( null != recommendListModel.getEffectiveTime()) {
            RecommendListModel rlm = recommendListService.findByEffectiveTime(recommendListModel.getEffectiveTime());
            if (null != rlm && !rlm.getId().equalsIgnoreCase(recommendListModel.getId())) {
                restResultModel.setResult(400);
                restResultModel.setResult_msg("存在相同相同的日期,请修改");
                return restResultModel;
            }
        }
        if(!StringUtils.isEmpty(recommendListModel.getVersion())) {
            RecommendListModel rlm = recommendListService.findByVersion(recommendListModel.getVersion());
            if( null != rlm && !rlm.getId().equalsIgnoreCase(recommendListModel.getId()) ){
                restResultModel.setResult(401);
                restResultModel.setResult_msg("存在相同相同的版本号,请修改");
                return restResultModel;
            }
        }
        recommendListService.update(recommendListModel);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RestResultModel get(String id){
        RestResultModel restResultModel = new RestResultModel();
        RecommendListModel recommendListModel = recommendListService.findById(id);
        restResultModel.setResult(200);
        restResultModel.setData(recommendListModel);
        return restResultModel;
    }
}
