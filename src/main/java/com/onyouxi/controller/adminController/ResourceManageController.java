package com.onyouxi.controller.adminController;

import com.onyouxi.model.dbModel.RecommendListModel;
import com.onyouxi.model.dbModel.ResourceModel;
import com.onyouxi.model.pageModel.PageResultModel;
import com.onyouxi.model.pageModel.RestResultModel;
import com.onyouxi.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/api/admin/resource")
@Slf4j
public class ResourceManageController extends BaseAdminController{

    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel findAll(Integer type,Integer pageNumber, Integer pageSize){
        pageNumber = pageNumber -1;
        if( null == type){
            type = 0;
        }
        Page<ResourceModel> resourcePage = resourceService.findAll(type,pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(resourcePage.getTotalElements());
        pageResultModel.setRows(resourcePage.getContent());
        return pageResultModel;
    }

    @RequestMapping(value = "/pageNameLike", method = RequestMethod.GET)
    public PageResultModel pageNameLike(String name,Integer pageNumber, Integer pageSize){
        pageNumber = pageNumber -1;
        Page<ResourceModel> resourcePage = resourceService.findByNameLike(name,pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(resourcePage.getTotalElements());
        pageResultModel.setRows(resourcePage.getContent());
        return pageResultModel;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RestResultModel get(String id){
        RestResultModel restResultModel = new RestResultModel();
        ResourceModel resourceModel = resourceService.findById(id);
        restResultModel.setResult(200);
        restResultModel.setData(resourceModel);
        return restResultModel;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResultModel update(ResourceModel resourceModel){
        RestResultModel restResultModel = new RestResultModel();
        resourceService.update(resourceModel);
        restResultModel.setResult(200);
        restResultModel.setResult_msg("success");
        return restResultModel;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id){
        RestResultModel restResultModel = new RestResultModel();
        resourceService.del(id);
        restResultModel.setResult(200);
        restResultModel.setResult_msg("success");
        return restResultModel;
    }

    @RequestMapping(value = "/addTag", method = RequestMethod.GET)
    public RestResultModel addTag(String id,Integer type,String tagId){
        RestResultModel restResultModel = new RestResultModel();
        ResourceModel resourceModel = new ResourceModel();
        resourceModel.setId(id);
        if( type==1){
            resourceModel.setCategory(Arrays.asList(tagId));
        }else{
            resourceModel.setTag(tagId);
        }
        ResourceModel result = resourceService.update(resourceModel);
        restResultModel.setResult(200);
        restResultModel.setResult_msg("success");
        restResultModel.setData(result);
        return restResultModel;
    }

    @RequestMapping(value = "/delTag", method = RequestMethod.GET)
    public RestResultModel delTag(String id,String category,Integer type){
        RestResultModel restResultModel = new RestResultModel();
        ResourceModel resourceModel = resourceService.delTag(id,category,type);
        restResultModel.setResult(200);
        restResultModel.setResult_msg("success");
        restResultModel.setData(resourceModel);
        return restResultModel;
    }


    @RequestMapping(value = "/createIcon", method = RequestMethod.POST)
    public RestResultModel createIcon(String id,String svgStr){
        RestResultModel restResultModel = new RestResultModel();
        resourceService.createIcon(id,svgStr);
        restResultModel.setResult(200);
        restResultModel.setResult_msg("success");

        return restResultModel;
    }


    @RequestMapping(value = "/idList", method = RequestMethod.POST)
    public RestResultModel getIdList(RecommendListModel recommendListModel){
        RestResultModel restResultModel = new RestResultModel();
        List<ResourceModel> resourceModelList =  resourceService.findByIdIn(recommendListModel.getResourceIdList());
        restResultModel.setResult(200);
        restResultModel.setResult_msg("success");
        restResultModel.setData(resourceModelList);
        return restResultModel;
    }



}
