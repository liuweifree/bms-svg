package com.onyouxi.service;

import com.alibaba.fastjson.JSON;
import com.onyouxi.constant.Const;
import com.onyouxi.model.dbModel.ResourceModel;
import com.onyouxi.repository.manager.ResourceRepository;
import com.onyouxi.utils.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ImageService imageService;

    @Value("${base.file.tmp.path}")
    private String tmpPath;

    @Value("${base.file.write.path}")
    private String savePath;

    public Page<ResourceModel> findAll(int type , int page,int size){
        Sort sort;
        if( type == 0){
            sort = Sort.by(Sort.Direction.DESC, "createTime");
        }else if( type == 1){
            sort = Sort.by( Sort.Order.desc("scoreMap.newScore"),Sort.Order.desc("createTime"));
        }else if( type ==2){
            sort = Sort.by( Sort.Order.desc("scoreMap.oldScore"),Sort.Order.desc("createTime"));
        }else{
            sort = Sort.by( Sort.Order.desc("showTime"));
        }

        PageRequest pageRequest = PageRequest.of(page, size,sort);
        return resourceRepository.findAll(pageRequest);
    }

    public Page<ResourceModel> findByNameLike(String name , int page,int size){
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");

        PageRequest pageRequest = PageRequest.of(page, size,sort);
        return resourceRepository.findByNameLike(name,pageRequest);
    }

    public ResourceModel findById(String id){
        return resourceRepository.findById(id).get();
    }

    public ResourceModel save(ResourceModel resourceModel){
        resourceModel.setStatus(Const.STATUS_INIT);
        resourceModel.setCreateTime(System.currentTimeMillis());
        resourceModel.setShowTime(0L);

        return resourceRepository.insert(resourceModel);
    }

    public ResourceModel update(ResourceModel resourceModel){
        ResourceModel old = findById(resourceModel.getId());
        if(CollectionUtils.isNotEmpty(resourceModel.getCategory())) {
            if(CollectionUtils.isNotEmpty(old.getCategory())){
                Set<String> set = new HashSet<>();
                set.addAll(old.getCategory());
                set.addAll(resourceModel.getCategory());
                old.setCategory(new ArrayList<>(set));
            }else {
                old.setCategory(resourceModel.getCategory());
            }
        }
        if(StringUtils.isNotEmpty(resourceModel.getTag())) {
            old.setTag(resourceModel.getTag());
        }
        if(StringUtils.isNotEmpty(resourceModel.getIcon())){
            old.setIcon(resourceModel.getIcon());
        }
        if(StringUtils.isNotEmpty(resourceModel.getResourceUrl())){
            old.setResourceUrl(resourceModel.getResourceUrl());
        }
        if(null != resourceModel.getStatus()){
            old.setStatus(resourceModel.getStatus());
        }
        if(StringUtils.isNotEmpty(resourceModel.getName())){
            old.setName(resourceModel.getName());
        }
        if(MapUtils.isNotEmpty(resourceModel.getScoreMap())){
            old.setScoreMap(resourceModel.getScoreMap());
        }
        if( null != resourceModel.getShowTime()){
            old.setShowTime(resourceModel.getShowTime());
        }
        if( null != resourceModel.getSvgUrl()){
            old.setSvgUrl(resourceModel.getSvgUrl());
        }

        if( null != resourceModel.getRecommend()){
            old.setRecommend(resourceModel.getRecommend());
        }

        resourceRepository.save(old);
        return old;
    }

    public ResourceModel delTag(String id,String category,Integer type){
        ResourceModel old = findById(id);
        if( type==1){
            if(CollectionUtils.isNotEmpty(old.getCategory())){
                old.getCategory().remove(category);
            }
        }else{
            old.setTag("");
        }
        resourceRepository.save(old);
        return old;
    }

    public void del(String id){
        resourceRepository.deleteById(id);
    }


    public Page<ResourceModel> findByStatus(Integer type,Integer num,Integer size){
        Sort sort;
       if( type == 1){
           sort = Sort.by( Sort.Order.desc("scoreMap.newScore"),Sort.Order.desc("createTime"));
        }else{
           sort = Sort.by( Sort.Order.desc("scoreMap.oldScore"),Sort.Order.desc("createTime"));
        }

        PageRequest pageRequest = PageRequest.of(num, size,sort);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return resourceRepository.findByStatusAndRecommendAndShowTimeBefore(Const.STATUS_NORMAL,0,calendar.getTimeInMillis(),pageRequest);
    }



    public Page<ResourceModel> findByStatusAndCategoryIsIn(Integer type,String category,Integer num, Integer size){
        Sort sort;
        if( type == 1){
            sort = Sort.by( Sort.Order.desc("scoreMap.newScore"),Sort.Order.desc("createTime"));
        }else{
            sort = Sort.by( Sort.Order.desc("scoreMap.oldScore"),Sort.Order.desc("createTime"));
        }

        PageRequest pageRequest = PageRequest.of(num, size,sort);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return resourceRepository.findByStatusAndRecommendAndCategoryIsInAndShowTimeBefore(Const.STATUS_NORMAL,0,category,calendar.getTimeInMillis(),pageRequest);
    }



    public List<ResourceModel> findAllByShowTime(Long startTime){
        if( null == startTime){
            startTime = System.currentTimeMillis();
        }
        Sort sort = Sort.by( Sort.Order.desc("showTime"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(startTime));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        calendar.add(Calendar.MONTH, - 13);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date(calendar.getTimeInMillis()));
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        Query query = new Query(
                Criteria.where("status").is(Const.STATUS_NORMAL)
                        .andOperator(
                        Criteria.where("showTime").lt(zero.getTime()),
                        Criteria.where("showTime").gt(calendar1.getTimeInMillis())));
        query.with(sort);
        return mongoTemplate.find(query,ResourceModel.class);
        //return resourceRepository.findByShowTimeGreaterThanAndShowTimeLessThanAndStatus(calendar1.getTimeInMillis(),zero.getTime(),Const.STATUS_NORMAL,sort);
    }

    public ResourceModel findByShowTime(Long showTime){
        return resourceRepository.findByShowTimeAndStatus(showTime,Const.STATUS_NORMAL);
    }


    public void createIcon(String id,String svgCode ){
        ResourceModel resourceModel = resourceRepository.findById(id).get();
        if( null != resourceModel){
            String iconFile = tmpPath+"/" + id+".png";
            String filePath = createImage(svgCode,iconFile,id);
            if( StringUtils.isNotEmpty(filePath)) {
                String oldIcon = resourceModel.getIcon();
                resourceModel.setIcon(filePath);
                resourceRepository.save(resourceModel);
                File file = new File(savePath+oldIcon);
                if(file.exists()){
                    file.delete();
                }
            }
        }

    }



    /**
     * 生成渐变色图片
     */
    private String createImage(String svgCode , String outFile,String id){
        FileOutputStream outputStream = null;
        try {
            File file = new File(outFile);
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            byte[] bytes = svgCode.getBytes("utf-8");
            PNGTranscoder t = new PNGTranscoder();
            TranscoderInput input = new TranscoderInput(
                    new ByteArrayInputStream(bytes));
            TranscoderOutput output = new TranscoderOutput(outputStream);
            t.transcode(input, output);
            outputStream.flush();
            Random random = new Random();
            int r = random.nextInt(100);
            String suffpex = outFile.substring(outFile.lastIndexOf("."));
            String resultFile = savePath+"/"+id+"_"+r+suffpex;
            imageService.changeQuality(outFile,resultFile,0.8f);
            return "/upload/"+id+"_"+r+suffpex;
        } catch (Exception e) {
            log.info("createImage error:{}",e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.info("createImage error:{}",e);
                }
            }
        }
        return null;
    }

    public List<ResourceModel> findByIdIn(List<String> idList){
        log.info("findByIdIn:{}", JSON.toJSONString(idList));
        List<ResourceModel> resourceModelList =  resourceRepository.findByIdIn(idList);
        if(CollectionUtils.isNotEmpty(resourceModelList)){
            Map<String,ResourceModel> resourceModelMap = new HashMap<>();
            List<ResourceModel> resourceModelList1 = new ArrayList<>();
            resourceModelList.forEach(resourceModel -> {
                resourceModelMap.put(resourceModel.getId(),resourceModel);
            });

            idList.forEach(id->{
                resourceModelList1.add(resourceModelMap.get(id));
            });
            return resourceModelList1;
        }

        return resourceModelList;
    }


    public ResourceModel findByName(String name){
        return resourceRepository.findByName(name);
    }


    public static void main(String[] args){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        System.out.println(calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH));

        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM");

        try {
            Date date = format2.parse(calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH));

            System.out.println(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.MONTH, - 13);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date(calendar.getTimeInMillis()));
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);

        System.out.println(calendar1.getTime());

    }
}
