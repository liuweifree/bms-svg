package com.onyouxi.repository.manager;

import com.onyouxi.model.dbModel.RecommendListModel;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecommendListRepository extends MongoRepository<RecommendListModel,String> {

    RecommendListModel findByEffectiveTime(Long effectiveTime);

    RecommendListModel findFirstByStatusAndTypeAndNewUserAndEffectiveTimeBefore(Integer status, Integer type,Integer newUser, Long effectiveTime, Sort sort);

    RecommendListModel findFirstByStatusAndTypeAndEffectiveTimeBefore(Integer status, Integer type, Long effectiveTime, Sort sort);
    List<RecommendListModel> findByStatus(Integer status);

    RecommendListModel findByVersion(String version);


}
