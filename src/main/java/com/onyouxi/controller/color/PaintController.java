package com.onyouxi.controller.color;


import com.onyouxi.constant.Const;
import com.onyouxi.model.dbModel.UserModel;
import com.onyouxi.model.pageModel.RestResultModel;
import com.onyouxi.model.view.EveryDay;
import com.onyouxi.model.view.EveryDayList;
import com.onyouxi.model.view.Paint;
import com.onyouxi.service.color.PaintService;
import com.onyouxi.service.color.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/color/paint")
@Slf4j
public class PaintController extends BaseUserController {

    @Autowired
    private PaintService paintService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/list")
    public RestResultModel getList(String category,Integer pageNum,Integer pageSize){
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(Const.SUCCESS);
        UserModel user = userService.getUserBySign(getUserToken());
        Integer userType = 0;
        if( null != user){
            if( null != user.getNewUser() && user.getNewUser() == 1){
                userType =1;
            }
        }
        if( null == pageSize){
            pageSize = 20;
        }
        if( null == pageNum ){
            pageNum =0;
        }else{
            pageNum = pageNum -1;
        }

        List<Paint> paintList = paintService.getPaintPage(category,pageNum,pageSize,userType);
        restResultModel.setData(paintList);
        return restResultModel;
    }

    @RequestMapping(value = "/daylist")
    public RestResultModel getDayList(String strategyId ,String lastId){
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(Const.SUCCESS);
        EveryDay everyDay = paintService.getEveryDay(strategyId,lastId);
        restResultModel.setData(everyDay);
        return restResultModel;
    }

    @RequestMapping(value = "/get")
    public RestResultModel get(String id){
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(Const.SUCCESS);
        Paint paint = paintService.getById(id);
        restResultModel.setData(paint);
        return restResultModel;
    }

}
