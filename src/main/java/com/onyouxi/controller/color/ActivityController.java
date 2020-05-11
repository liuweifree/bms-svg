package com.onyouxi.controller.color;

import com.onyouxi.constant.Const;
import com.onyouxi.model.dbModel.ActivityModel;
import com.onyouxi.model.pageModel.RestResultModel;
import com.onyouxi.model.view.Activity;
import com.onyouxi.model.view.Config;
import com.onyouxi.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/color/activity")
@Slf4j
public class ActivityController extends BaseUserController {

    @Autowired
    private ActivityService activityService;

    @Value("${file.url}")
    private String resourceUrl;

    @RequestMapping(value = "/banner")
    public RestResultModel getBanner(){
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(Const.SUCCESS);
        List<ActivityModel> activityModelList = activityService.findBanner(getLanguage());
        List<Activity> activityList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(activityModelList)){
            activityModelList.forEach(activityModel -> {
                Activity activity = new Activity();
                activity.setId(activityModel.getId());
                activity.setDesc(activityModel.getDesc());
                activity.setImgUrl(resourceUrl+"/"+activityModel.getImgUrl());
                activity.setTitle(activityModel.getTitle());
                activity.setDeeplink(activityModel.getProtocol());
                activityList.add(activity);
            });
        }
        restResultModel.setData(activityList);
        return restResultModel;
    }

    @RequestMapping(value = "/get")
    public RestResultModel getActivity(Integer page,Integer size){
        if( null == size){
            size = 20;
        }
        if( null == page){
            page = 0;
        }
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(Const.SUCCESS);
        Page<ActivityModel> activityModelPage = activityService.findActivity(getLanguage(),page,size);
        List<Activity> activityList = new ArrayList<>();
        if( null != activityModelPage && CollectionUtils.isNotEmpty(activityModelPage.getContent())){
            activityModelPage.getContent().forEach(activityModel -> {
                Activity activity = new Activity();
                activity.setId(activityModel.getId());
                activity.setDesc(activityModel.getDesc());
                activity.setImgUrl(resourceUrl+"/"+activityModel.getImgUrl());
                activity.setTitle(activityModel.getTitle());
                activity.setDeeplink(activityModel.getProtocol());
                activityList.add(activity);
            });
        }
        restResultModel.setData(activityList);
        return restResultModel;
    }

}
