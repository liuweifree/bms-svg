package com.onyouxi.repository.manager;

import com.onyouxi.model.dbModel.ConfigModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigRepository extends MongoRepository<ConfigModel,String> {

    ConfigModel findByType(Integer type);

    void deleteByType(Integer type);

}
