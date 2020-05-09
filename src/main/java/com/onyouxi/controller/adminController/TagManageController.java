package com.onyouxi.controller.adminController;

import com.onyouxi.model.dbModel.ResourceModel;
import com.onyouxi.model.dbModel.TagModel;
import com.onyouxi.model.pageModel.PageResultModel;
import com.onyouxi.model.pageModel.RestResultModel;
import com.onyouxi.service.ResourceService;
import com.onyouxi.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/api/admin/tag")
@Slf4j
public class TagManageController extends BaseAdminController{

    @Autowired
    private TagService tagService;

    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel findAll(Integer type,Integer pageNumber, Integer pageSize){
        pageNumber = pageNumber -1;
        Page<TagModel> tagPage = tagService.findByType(type,pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(tagPage.getTotalElements());
        pageResultModel.setRows(tagPage.getContent());
        return pageResultModel;
    }

    @RequestMapping(value = "/pageNotIn", method = RequestMethod.GET)
    public PageResultModel findIdNotId(Integer type,String resourceId,Integer pageNumber, Integer pageSize){
        pageNumber = pageNumber -1;

        ResourceModel resourceModel =  resourceService.findById(resourceId);
        List<String> idList = new ArrayList<>();
        if(type==1){
            idList.addAll(resourceModel.getCategory());
        }else{
            idList.add(resourceModel.getTag());
        }

        Page<TagModel> tagPage = tagService.findByTypeAndIdNotIn(type,idList,pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(tagPage.getTotalElements());
        pageResultModel.setRows(tagPage.getContent());
        return pageResultModel;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel save(TagModel tagModel){
        RestResultModel restResultModel = new RestResultModel();
        Integer result = tagService.saveTag(tagModel);

        if(result == 1){
            restResultModel.setResult(400);
            restResultModel.setResult_msg("程序标记不能重复");
        }else{
            restResultModel.setResult(200);
            restResultModel.setResult_msg("success");
        }

        return restResultModel;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id){
        RestResultModel restResultModel = new RestResultModel();
        tagService.delTag(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResultModel update(TagModel tagModel){
        RestResultModel restResultModel = new RestResultModel();
        Integer result = tagService.updateTag(tagModel);
        if(result == 1){
            restResultModel.setResult(400);
            restResultModel.setResult_msg("程序标记不能重复");
        }else{
            restResultModel.setResult(200);
            restResultModel.setResult_msg("success");
        }
        return restResultModel;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RestResultModel get(String id){
        RestResultModel restResultModel = new RestResultModel();
        TagModel tagModel = tagService.findById(id);
        restResultModel.setResult(200);
        restResultModel.setData(tagModel);
        return restResultModel;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public RestResultModel all(){
        RestResultModel restResultModel = new RestResultModel();
        List<TagModel> tagModelList = tagService.findAll();
        restResultModel.setResult(200);
        restResultModel.setData(tagModelList);
        return restResultModel;
    }

}
