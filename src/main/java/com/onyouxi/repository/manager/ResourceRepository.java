package com.onyouxi.repository.manager;

import com.onyouxi.model.dbModel.ResourceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface ResourceRepository extends MongoRepository<ResourceModel,String> {

    Page<ResourceModel> findByNameLike(String name, Pageable pageable);

    Page<ResourceModel> findByStatusAndRecommendAndShowTimeBefore(Integer status,Integer recommend,Long showTime,Pageable pageable);

    Page<ResourceModel> findByStatusAndRecommendAndCategoryIsInAndShowTimeBefore(Integer status,Integer recommend,String category ,Long showTime,Pageable pageable);

    Page<ResourceModel> findByStatusAndRecommendAndCategoryIsInAndShowTimeBeforeAndVerifyStatus(Integer status,Integer recommend,String category ,Long showTime,Integer verifyStatus,Pageable pageable);

    List<ResourceModel> findByShowTimeGreaterThanAndShowTimeLessThanAndStatus(Long startShowTime,Long endTime, Integer status, Sort sort);

    ResourceModel findByShowTimeAndStatus(Long showTime,Integer status);

    ResourceModel findByShowTime(Long showTime);


    List<ResourceModel> findByIdIn(List<String> idList);

    ResourceModel findByName(String name);


}
