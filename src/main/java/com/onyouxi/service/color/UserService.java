package com.onyouxi.service.color;

import com.onyouxi.constant.Const;
import com.onyouxi.model.dbModel.UserModel;
import com.onyouxi.repository.color.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public UserModel loginOrRegiste(UserModel user){
        UserModel u = userRepository.findBySign(user.getSign());
        if( null != u){
            if( null !=  u.getStatus() && u.getStatus().intValue() != Const.STATUS_NORMAL){
                return null;
            }
            log.info("registe user:{}",u.getSign());
            u.setLoginTime(System.currentTimeMillis());
            save(u);
            return u;
        }else{
            save(user);
            return user;
        }

    }

    public void save(UserModel user){
        user.setCreateTime(System.currentTimeMillis());
        if( StringUtils.isEmpty(user.getId())) {
            user.setStatus(Const.STATUS_NORMAL);
            user.setNewUser(0);
        }
        userRepository.save(user);
    }

    public UserModel getUserBySign(String sign){
        return userRepository.findBySignAndStatus(sign, Const.STATUS_NORMAL);
    }

    /**
     * 更新用户的订阅过期时间
     * @param sign
     * @param expire
     */
    public void updateUserPayExpires(String sign , Long expire){
        UserModel userModel = userRepository.findBySign(sign);
        userModel.setPayExpires(expire);
        userRepository.save(userModel);
    }

    /**
     * 更新最后登录时间
     * @param sign
     */
    public void updateUserLoginTime(String sign ){
        UserModel userModel = userRepository.findBySign(sign);
        userModel.setLoginTime(System.currentTimeMillis());
        userRepository.save(userModel);
    }

    public void updateUserOriginalTransactionId(String sign  , String originalTransactionId){
        UserModel userModel = userRepository.findBySign(sign);
        if(StringUtils.isEmpty(userModel.getOriginalTransactionId())) {
            userModel.setOriginalTransactionId(originalTransactionId);
            userRepository.save(userModel);
        }

    }

    /**
     * 按照原始订单号查询
     * @param originalTransactionId
     * @return
     */
    public UserModel findByOriginalTransactionId(String originalTransactionId){
        return userRepository.findByOriginalTransactionId(originalTransactionId);
    }

    /**
     * 查询过期时间段内的用户
     * @param startTime
     * @param endTime
     * @return
     */
    public List<UserModel> findByPayExpiresBetween(Long startTime,Long endTime){
        return userRepository.findByPayExpiresBetween(startTime,endTime);
    }

    /**
     * 查询扣款失败的用户
     * @param payStatus
     * @return
     */
    public List<UserModel> findByPayStatus(Integer payStatus){
        return userRepository.findByPayStatus(payStatus);
    }

    /**
     * 恢复购买
     * @param sign
     * @param originalTransactionId
     */
    public void restore(String sign,String originalTransactionId){
        UserModel userModel = getUserBySign(sign);
        if( null != userModel){

        }
        UserModel newUserModel = this.findByOriginalTransactionId(originalTransactionId);
        if( null == newUserModel){

        }
    }


    public Integer updateNewUser(String sign){
        UserModel userModel = userRepository.findBySign(sign);
        if( null != userModel){
            userModel.setNewUser(1);
            userRepository.save(userModel);
            return 0;
        }else{
            return -1;
        }

    }


}
