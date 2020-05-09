package com.onyouxi.service;

import com.onyouxi.constant.Const;
import com.onyouxi.model.dbModel.ActivityModel;
import com.onyouxi.repository.manager.ActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public void save(ActivityModel activityModel){
        activityModel.setStatus(Const.STATUS_INIT);
        activityModel.setCreateTime(System.currentTimeMillis());
        activityRepository.save(activityModel);
    }

    public Page<ActivityModel> findAll(Integer type ,int size,int page){
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = PageRequest.of(page, size,sort);
        if( null == type) {
            return activityRepository.findAll(pageRequest);
        }else{
            return activityRepository.findByType(type,pageRequest);
        }
    }

    public void update(ActivityModel activityModel){
        ActivityModel old = activityRepository.findById(activityModel.getId()).get();
        if( null != old){
            if(StringUtils.isNotEmpty(activityModel.getDesc())){
                old.setDesc(activityModel.getDesc());
            }
            if(StringUtils.isNotEmpty(activityModel.getTitle())){
                old.setTitle(activityModel.getTitle());
            }
            if(StringUtils.isNotEmpty(activityModel.getImgUrl())){
                old.setImgUrl(activityModel.getImgUrl());
            }
            if(StringUtils.isNotEmpty(activityModel.getProtocol())){
                old.setProtocol(activityModel.getProtocol());
            }
            if( null != activityModel.getSort()){
                old.setSort(activityModel.getSort());
            }
            if( null != activityModel.getStatus()){
                old.setStatus( activityModel.getStatus());
            }
            if( null != activityModel.getType()){
                old.setType(activityModel.getType());
            }
            activityRepository.save(old);
        }
    }


    public void del(String id){
        activityRepository.deleteById(id);

    }

    public ActivityModel findById(String id){
        return activityRepository.findById(id).get();
    }

    public List<ActivityModel> findBanner(String language){
        Sort sort =Sort.by( Sort.Order.desc("sort"),Sort.Order.desc("createTime"));
        return activityRepository.findByTypeAndLanguageAndStatus(0,language,Const.STATUS_NORMAL,sort);
    }

    public Page<ActivityModel> findActivity(String language,Integer page,Integer size){
        Sort sort =Sort.by( Sort.Order.desc("sort"),Sort.Order.desc("createTime"));
        PageRequest pageRequest = PageRequest.of(page, size,sort);
        return activityRepository.findByTypeAndLanguageAndStatus(1,language,Const.STATUS_NORMAL,pageRequest);
    }

}
