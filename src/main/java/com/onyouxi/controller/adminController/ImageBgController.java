package com.onyouxi.controller.adminController;

import com.onyouxi.model.dbModel.ImageBgModel;
import com.onyouxi.model.pageModel.PageResultModel;
import com.onyouxi.model.pageModel.RestResultModel;
import com.onyouxi.service.ImageBgService;
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
import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/v1/api/admin/background")
public class ImageBgController {

    @Autowired
    private ImageBgService imageBgService;

    @Value("${base.file.background.path}")
    private String savePath;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel findAll(Integer pageNumber, Integer pageSize){
        pageNumber = pageNumber -1;
        Page<ImageBgModel> ImageBgPage = imageBgService.findAll(pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(ImageBgPage.getTotalElements());
        pageResultModel.setRows(ImageBgPage.getContent());
        return pageResultModel;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel save(ImageBgModel imageBgModel){
        RestResultModel restResultModel = new RestResultModel();
        imageBgService.save(imageBgModel);
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
            String suffpex = fileName.substring(fileName.lastIndexOf("."));
            long time = System.currentTimeMillis();
            String resultFileName = time +suffpex;
            String targetFilePath = savePath+"/"+resultFileName;
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(targetFilePath)));
            stream.write(bytes);
            stream.close();

            restResultModel.setResult(200);
            restResultModel.setData(resultFileName);
        }catch (Exception e){
            log.error("upload error:{}",e);
        }
        return restResultModel;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id){
        RestResultModel restResultModel = new RestResultModel();
        imageBgService.delete(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RestResultModel list(){
        RestResultModel restResultModel = new RestResultModel();
        List<ImageBgModel> ImageBgList = imageBgService.findAll();
        restResultModel.setResult(200);
        restResultModel.setData(ImageBgList);
        return restResultModel;
    }


}
