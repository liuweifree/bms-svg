package com.onyouxi.repository.manager;

import com.onyouxi.model.dbModel.EveryDayModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface EveryDayRepository extends MongoRepository<EveryDayModel,String> {

    List<EveryDayModel> findByMonthBetween(Date fromDate,Date toDate);



}
