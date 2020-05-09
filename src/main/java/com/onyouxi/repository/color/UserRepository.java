package com.onyouxi.repository.color;

import com.onyouxi.model.dbModel.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<UserModel,String> {

    UserModel findBySign(String sign);

    UserModel findBySignAndStatus(String sign,Integer status);

    UserModel findByOriginalTransactionId(String originalTransactionId);

    List<UserModel> findByPayExpiresBetween(Long startTime,Long endTime);

    List<UserModel> findByPayStatus(Integer payStatus);

}
