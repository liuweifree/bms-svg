package com.onyouxi.controller.fileprocess;

import com.onyouxi.constant.Const;
import com.onyouxi.model.dbModel.ResourceModel;
import com.onyouxi.model.pageModel.RestResultModel;
import com.onyouxi.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@RestController
@RequestMapping(value = "/api/fileprocess/resource")
@Slf4j
public class ResourceController {

    @Value("${base.file.write.path}")
    private String savePath;

    @Value("${base.file.svg.path}")
    private String saveSvgPath;

    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public RestResultModel save(String pwd,Integer type,String name){
        RestResultModel restResultModel = new RestResultModel();
        if(StringUtils.isEmpty(pwd)){
            restResultModel.setResult(200);
            return restResultModel;
        }
        if( !Const.PWD.equals(pwd)){
            restResultModel.setResult(200);
            return restResultModel;
        }
        ResourceModel resourceModel = new ResourceModel();
        resourceModel.setType(type);
        resourceModel.setRecommend(0);
        resourceModel.setName(name);
        ResourceModel rm = resourceService.save(resourceModel);
        restResultModel.setResult(200);
        restResultModel.setData(rm);
        return restResultModel;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResultModel update(ResourceModel resourceModel){
        RestResultModel restResultModel = new RestResultModel();
        resourceService.update(resourceModel);
        restResultModel.setResult(200);
        restResultModel.setResult_msg("success");
        return restResultModel;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public RestResultModel fileUpload(@RequestParam("Filedata") MultipartFile file, String resourceId,Integer type,String name) throws FileUploadException {
        RestResultModel restResultModel = new RestResultModel();
        try {
            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            String suffpex = fileName.substring(fileName.lastIndexOf("."));
            String targetFilePath = "";
            if( type == 3){
                targetFilePath = saveSvgPath+"/"+resourceId +suffpex;
            }else{
                targetFilePath = savePath+"/"+resourceId +suffpex;
            }


            String fileUrl = "/upload/"+resourceId +suffpex;
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(targetFilePath)));
            stream.write(bytes);
            stream.close();

            ResourceModel resourceModel = new ResourceModel();
            resourceModel.setId(resourceId);
            if(type==1) {
                resourceModel.setIcon(fileUrl);
            }else if(type==2){
                resourceModel.setResourceUrl(fileUrl);
            }else if(type==3){
                fileUrl = "/bgsvg/"+resourceId +suffpex;
                resourceModel.setSvgUrl(fileUrl);
            }

            resourceModel.setName(name);
            resourceModel.setStatus(Const.STATUS_NORMAL);
            resourceModel.setShowTime(1588608000000L);
            resourceService.update(resourceModel);
        }catch (Exception e){
            log.error("upload error:{}",e);
        }
        return restResultModel;
    }

    @RequestMapping(value = "/findByName", method = RequestMethod.GET)
    public RestResultModel findByName(String name){
        RestResultModel restResultModel = new RestResultModel();
        ResourceModel resourceModel = resourceService.findByName(name);
        if( null != resourceModel){
            restResultModel.setData(resourceModel.getId());
        }
        restResultModel.setResult(200);
        restResultModel.setResult_msg("success");
        return restResultModel;
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public RestResultModel uploadFile(@RequestParam("Filedata") MultipartFile file, String resourceId,Integer type) throws FileUploadException {
        RestResultModel restResultModel = new RestResultModel();
        try {
            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            String suffpex = fileName.substring(fileName.lastIndexOf("."));
            String targetFilePath = "";
            if (type == 3) {
                targetFilePath = saveSvgPath + "/" + resourceId + suffpex;
            } else {
                targetFilePath = savePath + "/" + resourceId + suffpex;
            }
            File oldFile = new File(targetFilePath);
            if(oldFile.exists()){
                oldFile.delete();
            }
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(targetFilePath)));
            stream.write(bytes);
            stream.close();
        }catch (Exception e){
            log.error("upload error:{}",e);
        }

        return restResultModel;
    }

    public static void main(String[] args){

        String a ="/Users/liuwei/aaa.png";

        System.out.println(a.substring(a.lastIndexOf(".")));
    }

}
