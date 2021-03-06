package com.onyouxi.filter;

import com.alibaba.fastjson.JSON;
import com.onyouxi.constant.Const;
import com.onyouxi.controller.adminController.BaseAdminController;
import com.onyouxi.controller.color.BaseUserController;
import com.onyouxi.model.dbModel.AdminUserModel;
import com.onyouxi.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by administrator on 2016/11/24.
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminUserService adminUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if( o instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) o;
            String url = request.getRequestURI();
            String cookieValue = "";

            //判断是否需要登录验证
            if( url.indexOf("/v1/api/admin") != -1 || url.equals("/v1/logout")){
                Cookie[] cookies =request.getCookies();
                if( null != cookies) {
                    for (Cookie cookie : cookies) {
                        if (Const.COOKIE_AUTH_KEY.equals(cookie.getName())) {
                            cookieValue = cookie.getValue();
                        }
                    }
                    //对auth_key进行校验
                    if (!StringUtils.isEmpty(cookieValue)) {
                        if (!adminUserService.checkAuthKey(cookieValue)) {
                            response.setStatus(444);
                            return false;
                        } else {
                            //每次操作后增加session时间
                            adminUserService.addSessionTime(cookieValue, response);
                        }
                    } else {
                        response.setStatus(444);
                        return false;
                    }
                }else{
                    response.setStatus(444);
                    return false;
                }

                AdminUserModel adminUser = adminUserService.getAdminUser(cookieValue);
                //注入adminUser
                if( handlerMethod.getBean() instanceof BaseAdminController){
                    BaseAdminController baseAdminController = (BaseAdminController) handlerMethod.getBean();
                    baseAdminController.setAdminUser(adminUser);
                }
            }

            if(url.indexOf("/api/color") != -1){
                if( handlerMethod.getBean() instanceof BaseUserController){
                    String language = request.getHeader("language");
                    if(StringUtils.isEmpty(language)){
                        language = "en_US";
                    }else{
                        if(!language.equals("en_US") && !language.equals("zh_CN")){
                            language = "en_US";
                        }
                    }
                    String userToken = request.getHeader("userToken");
                    if(StringUtils.isEmpty(userToken)){
                        userToken = "";
                    }
                    BaseUserController baseUserController = (BaseUserController) handlerMethod.getBean();
                    baseUserController.setLanguage(language);
                    baseUserController.setUserToken(userToken);

                    try {
                        log.info("url:{},userToken:{},param:{}", url, userToken, JSON.toJSONString(request.getParameterMap()));
                    }catch (Exception e){
                        log.error("AuthInterceptor error:{}",e);
                    }
                }


            }


        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
