package com.onyouxi.service;

import com.alibaba.fastjson.JSON;
import com.onyouxi.constant.Const;
import com.onyouxi.model.dbModel.RecommendListModel;
import com.onyouxi.model.dbModel.ResourceModel;
import com.onyouxi.repository.manager.RecommendListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RecommendListService {

    @Autowired
    private RecommendListRepository recommendListRepository;

    @Autowired
    private ResourceService resourceService;


    private Map<String,RecommendListModel> CACHEMAP = new ConcurrentHashMap<>();

    private String MAP_KEY = "idList";

    @PostConstruct
    private void init(){
        reloadCache();
    }

    public Page<RecommendListModel> findAll(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return recommendListRepository.findAll(pageRequest);
    }

    public void save(RecommendListModel recommendListModel){
        recommendListModel.setCreateTime(System.currentTimeMillis());
        recommendListModel.setStatus(Const.STATUS_INIT);
        recommendListRepository.save(recommendListModel);
    }

    public void delete(String id){
        recommendListRepository.deleteById(id);
    }

    public List<RecommendListModel> findAll(){
        return recommendListRepository.findByStatus(Const.STATUS_NORMAL);
    }


    public void update(RecommendListModel recommendListModel){

        log.info("update:{}", JSON.toJSONString(recommendListModel));
        RecommendListModel old = recommendListRepository.findById(recommendListModel.getId()).get();
        if( null != old){
            if( null != recommendListModel.getStatus()){
                old.setStatus(recommendListModel.getStatus());
            }
            if( null != recommendListModel.getName()){
                old.setName(recommendListModel.getName());
            }
            if( null != recommendListModel.getEffectiveTime()){
                old.setEffectiveTime(recommendListModel.getEffectiveTime());
            }

            if( null != recommendListModel.getIntervalTime()){
                old.setIntervalTime(recommendListModel.getIntervalTime());
            }

            if( null != recommendListModel.getType()){
                old.setType(recommendListModel.getType());
            }

            if( null != recommendListModel.getVerifyStatus()){
                old.setVerifyStatus(recommendListModel.getVerifyStatus());
            }

            if(!CollectionUtils.isEmpty(recommendListModel.getResourceIdList())){
                List<String> oldResourceIdList = old.getResourceIdList();
                if(!CollectionUtils.isEmpty(oldResourceIdList)) {
                    oldResourceIdList.forEach(resourceId->{
                        ResourceModel resourceModel = new ResourceModel();
                        resourceModel.setId(resourceId);
                        resourceModel.setRecommend(0);
                        resourceService.update(resourceModel);
                    });
                }
                recommendListModel.getResourceIdList().forEach(resourceId->{
                    ResourceModel resourceModel = new ResourceModel();
                    resourceModel.setId(resourceId);
                    resourceModel.setRecommend(1);
                    resourceService.update(resourceModel);
                });

                old.setResourceIdList(recommendListModel.getResourceIdList());
            }

            if( null != recommendListModel.getNewUser()){
                old.setNewUser(recommendListModel.getNewUser());
            }
            recommendListRepository.save(old);
            reloadCache();
        }
    }

    public RecommendListModel findById(String id){
        return recommendListRepository.findById(id).get();
    }

    public RecommendListModel findByEffectiveTime(Long effectiveTime){
        return recommendListRepository.findByEffectiveTime(effectiveTime);
    }

    public RecommendListModel findByVerifyStatus(Integer verifyStatus,Integer type){
        return recommendListRepository.findFirstByStatusAndTypeAndVerifyStatus(Const.STATUS_NORMAL,type,verifyStatus);
    }

    public RecommendListModel findByVerifyStatusAndType(Integer verifyStatus,Integer type){
        return recommendListRepository.findByVerifyStatusAndType(verifyStatus,type);
    }



    public List<String> cacheGet(String id){
        RecommendListModel recommendListModel;
        if(StringUtils.isEmpty(id)){
            recommendListModel= CACHEMAP.get(MAP_KEY);
        }else{
            recommendListModel= CACHEMAP.get(id);
        }

        if( null ==recommendListModel ){
            recommendListModel = findEyeryDay();
            CACHEMAP.put(MAP_KEY,recommendListModel);
            CACHEMAP.put(recommendListModel.getId(),recommendListModel);
        }
        return recommendListModel.getResourceIdList();
    }

    public RecommendListModel cacheGetRecommend(){
        return CACHEMAP.get(MAP_KEY);
    }

    public void reloadCache(){
        RecommendListModel recommendListModel = findEyeryDay();
        if( null != recommendListModel) {
            log.info("reloadCache:{}", JSON.toJSONString(recommendListModel));
            CACHEMAP.put(MAP_KEY, recommendListModel);
        }

        List<RecommendListModel> recommendListModelList = this.findAll();
        if( !CollectionUtils.isEmpty(recommendListModelList)){
            recommendListModelList.forEach(recommendListModel1 -> CACHEMAP.put(recommendListModel1.getId(),recommendListModel1));
        }
    }

    public Integer currentResourceIndex(String strategyId , String id){
        return cacheGet(strategyId).indexOf(id);
    }

    public String getCacheNext(String strategyId ,String id,Integer interval){
        Integer index = 0;
        String mapId;
        if(StringUtils.isEmpty(strategyId)){
            mapId = MAP_KEY;
        }else{
            mapId = strategyId;
        }
        if( !StringUtils.isEmpty(id)){
            index = currentResourceIndex(strategyId,id);
        }else{
            index = -1;
        }
        if( index < 0){
            return CACHEMAP.get(mapId).getResourceIdList().get(interval-1);
        }
        if( index >= CACHEMAP.get(mapId).getResourceIdList().size()){
            return null;
        }
        if( index+interval >= CACHEMAP.get(mapId).getResourceIdList().size()){
            return null;
        }

        return CACHEMAP.get(mapId).getResourceIdList().get(index+interval);

    }

    /**
     * 获取每日
     * @return
     */
    public RecommendListModel findEyeryDay(){
        Sort sort =Sort.by( Sort.Order.desc("sort"),Sort.Order.desc("effectiveTime"));
        return recommendListRepository.findFirstByStatusAndTypeAndEffectiveTimeBefore(Const.STATUS_NORMAL,1,System.currentTimeMillis(),sort);
    }

    /**
     * 获取推荐
     * @return
     */
    public RecommendListModel findRecommend(Integer newUser){
        Sort sort =Sort.by( Sort.Order.desc("sort"),Sort.Order.desc("effectiveTime"));
        return recommendListRepository.findFirstByStatusAndTypeAndNewUserAndEffectiveTimeBefore(Const.STATUS_NORMAL,0,newUser,System.currentTimeMillis(),sort);
    }

}
