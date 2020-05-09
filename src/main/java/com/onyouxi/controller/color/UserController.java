package com.onyouxi.controller.color;

import com.onyouxi.constant.Const;
import com.onyouxi.model.dbModel.UserModel;
import com.onyouxi.model.pageModel.RestResultModel;
import com.onyouxi.service.color.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "/api/color/user")
public class UserController extends BaseUserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login")
    public RestResultModel login(UserModel user){
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(Const.SUCCESS);
        restResultModel.setResult_msg("success");
        user.setSign(getUserToken());
        UserModel userModel = userService.loginOrRegiste(user);
        if( null == userModel){
            restResultModel.setResult(Const.ILLEGAL_USER);
            restResultModel.setResult_msg("非法的用户");
        }else{
            Map<String,Long> resultMap = new HashMap<>();
            resultMap.put("expiresDate",userModel.getPayExpires());
            restResultModel.setData(resultMap);
        }
        return restResultModel;
    }

    @RequestMapping(value = "/newuser")
    public RestResultModel updateNewUser(){
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(Const.SUCCESS);
        restResultModel.setResult_msg("success");
        userService.updateNewUser(getUserToken());
        return restResultModel;
    }

}
