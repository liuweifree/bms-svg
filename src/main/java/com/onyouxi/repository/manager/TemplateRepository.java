package com.onyouxi.repository.manager;

import com.onyouxi.model.dbModel.TemplateModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TemplateRepository extends MongoRepository<TemplateModel,String> {
}
