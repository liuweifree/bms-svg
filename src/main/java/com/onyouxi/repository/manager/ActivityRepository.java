package com.onyouxi.repository.manager;

import com.onyouxi.model.dbModel.ActivityModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActivityRepository extends MongoRepository<ActivityModel,String> {

    Page<ActivityModel> findByType(Integer type,Pageable pageable);

    Page<ActivityModel> findByTypeAndLanguageAndStatus(Integer type,String language,Integer status,Pageable pageable);

    List<ActivityModel> findByTypeAndLanguageAndStatus(Integer type, String language, Integer status, Sort sort);
}
