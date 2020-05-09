package com.onyouxi.controller.color;

import com.alibaba.fastjson.JSONObject;
import com.onyouxi.constant.Const;
import com.onyouxi.model.pageModel.RestResultModel;
import com.onyouxi.model.view.Pay;
import com.onyouxi.service.color.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/color/pay")
@Slf4j
public class PayController extends BaseUserController {

    @Autowired
    private PayService payService;

    @RequestMapping(value = "/do")
    public RestResultModel pay(@RequestBody JSONObject jsonParam){
        log.info("userToken:{} ,receipt:{}",getUserToken(),jsonParam.getString("receipt"));
        RestResultModel restResultModel = new RestResultModel();
        String receipt = jsonParam.getString("receipt");
        if(StringUtils.isEmpty(getUserToken())){
            restResultModel.setResult(Const.ILLEGAL_USER);
            return restResultModel;
        }
        if(StringUtils.isEmpty(receipt)){
            restResultModel.setResult(Const.PAY_ERROR);
            return restResultModel;
        }
        Long result = payService.applePay(getUserToken(),receipt);
        if(result != 1) {
            restResultModel.setResult(Const.SUCCESS);
            Map<String,Long> resultMap = new HashMap<>();
            resultMap.put("expiresDate",result);
            restResultModel.setData(resultMap);
        }else{
            restResultModel.setResult(Const.PAY_ERROR);
        }

        return restResultModel;
    }


    @RequestMapping(value = "/appleNotify")
    public RestResultModel appleNotify(@RequestBody String body){
        RestResultModel restResultModel = new RestResultModel();
        log.info(body);
        return restResultModel;
    }



}
