package com.onyouxi.controller.adminController;

import com.onyouxi.model.dbModel.ActivityModel;
import com.onyouxi.model.dbModel.ResourceModel;
import com.onyouxi.model.pageModel.PageResultModel;
import com.onyouxi.model.pageModel.RestResultModel;
import com.onyouxi.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@RestController
@RequestMapping(value = "/v1/api/admin/activity")
@Slf4j
public class ActivityManageController extends BaseAdminController {

    @Value("${base.file.activity.path}")
    private String saveActivityPath;

    @Autowired
    private ActivityService activityService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel findAll(Integer type, Integer pageNumber, Integer pageSize){
        pageNumber = pageNumber -1;
        if( null == type){
            type = 0;
        }
        Page<ActivityModel> resourcePage = activityService.findAll(type,pageSize,pageNumber);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(resourcePage.getTotalElements());
        pageResultModel.setRows(resourcePage.getContent());
        return pageResultModel;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RestResultModel get(String id){
        RestResultModel restResultModel = new RestResultModel();
        ActivityModel activityModel = activityService.findById(id);
        restResultModel.setResult(200);
        restResultModel.setData(activityModel);
        return restResultModel;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel save(ActivityModel activityModel){
        RestResultModel restResultModel = new RestResultModel();
        activityService.save(activityModel);
        restResultModel.setResult(200);
        restResultModel.setData(activityModel);
        return restResultModel;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResultModel update(ActivityModel activityModel){
        RestResultModel restResultModel = new RestResultModel();
        activityService.update(activityModel);
        restResultModel.setResult(200);
        restResultModel.setResult_msg("success");
        return restResultModel;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id){
        RestResultModel restResultModel = new RestResultModel();
        activityService.del(id);
        restResultModel.setResult(200);
        restResultModel.setResult_msg("success");
        return restResultModel;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public RestResultModel fileUpload(@RequestParam("Filedata") MultipartFile file) throws FileUploadException {
        RestResultModel restResultModel = new RestResultModel();
        try {
            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            String targetFilePath = saveActivityPath+"/"+System.currentTimeMillis() +suffix;
            String fileUrl = "activity/"+System.currentTimeMillis() +suffix;
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(targetFilePath)));
            stream.write(bytes);
            stream.close();
            restResultModel.setResult(200);
            restResultModel.setData(fileUrl);
        }catch (Exception e){
            log.error("upload error:{}",e);
        }
        return restResultModel;
    }

}
