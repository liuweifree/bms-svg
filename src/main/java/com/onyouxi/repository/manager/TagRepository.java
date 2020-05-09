package com.onyouxi.repository.manager;

import com.onyouxi.model.dbModel.TagModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TagRepository extends MongoRepository<TagModel,String> {

    Page<TagModel> findByType(int type , Pageable pageable);

    TagModel findByKeyValue(String keyValue);

    TagModel findByKeyValueAndIdIsNot(String keyValue,String id);

    Page<TagModel> findByTypeAndIdNotIn(int type ,List<String> idList,Pageable pageable);

    List<TagModel> findByStatus(Integer status);
}
