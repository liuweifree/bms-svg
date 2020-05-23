package com.onyouxi.service.color;

import com.alibaba.fastjson.JSON;
import com.onyouxi.model.dbModel.RecommendListModel;
import com.onyouxi.model.dbModel.ResourceModel;
import com.onyouxi.model.dbModel.TagModel;
import com.onyouxi.model.view.EveryDay;
import com.onyouxi.model.view.EveryDayList;
import com.onyouxi.model.view.Paint;
import com.onyouxi.service.RecommendListService;
import com.onyouxi.service.ResourceService;
import com.onyouxi.service.TagService;
import com.onyouxi.service.VerifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Slf4j
@Service
public class PaintService {

    @Value("${file.url}")
    private String resourceUrl;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private RecommendListService recommendListService;

    @Autowired
    private TagService tagService;

    @Autowired
    private VerifyService verifyService;



    public List<Paint> getPaintPage(String category,Integer pageNum , Integer pageSize,Integer userType){
        List<Paint> paintList = new ArrayList<>();
        Page<ResourceModel> page = null;
        if(StringUtils.isEmpty(category)){
            page=resourceService.findByStatus(userType,pageNum,pageSize);
        }else{

            //走推荐逻辑
            if( !StringUtils.isEmpty(category) && category.equals("recommend_list")){
                RecommendListModel recommendListModel;
                //走审核逻辑
                if( verifyService.getVerifyStatus().intValue() == 1){
                    recommendListModel = recommendListService.findByVerifyStatus(verifyService.getVerifyStatus(),0);
                }else {
                    recommendListModel = recommendListService.findRecommend(userType);
                }
                if( null != recommendListModel ){
                    List<ResourceModel> resourceModelList = new ArrayList<>();
                    List<String> resourceIdList = recommendListModel.getResourceIdList();
                    //如果是第一页 并且pageSize> 总数 直接返回
                    if( pageNum ==0 && pageSize >= resourceIdList.size()){
                        resourceModelList = resourceService.findByIdIn(resourceIdList);
                    }else{
                        int startIndex = pageNum*pageSize;
                        int endIndex = (pageNum+1)*pageSize-1;
                        if(startIndex <   resourceIdList.size() -1 ){
                            if( endIndex <= resourceIdList.size() -1){
                                resourceModelList = resourceService.findByIdIn(resourceIdList.subList(startIndex,endIndex));
                            }else{
                                resourceModelList = resourceService.findByIdIn(resourceIdList.subList(startIndex,resourceIdList.size()-1));
                            }
                        }
                    }

                    if(CollectionUtils.isNotEmpty(resourceModelList)){
                        resourceModelList.forEach(resourceModel -> {
                            if( null != resourceModel) {
                                Paint paint = new Paint();
                                paint.setCategory(resourceModel.getCategory());
                                paint.setId(resourceModel.getId());
                                paint.setPngUrl(resourceUrl + resourceModel.getIcon());
                                paint.setZipUrl(resourceUrl + resourceModel.getResourceUrl());
                                paint.setTagKey(resourceModel.getTag());
                                paint.setType(resourceModel.getType());
                                paint.setStatus(resourceModel.getStatus());
                                paint.setCreateTime(resourceModel.getCreateTime());
                                paint.setType(resourceModel.getType());
                                paint.setShowTime(resourceModel.getShowTime());
                                paintList.add(paint);
                            }
                        });
                    }
                    return paintList;
                }
            }

            //走审核逻辑
            if( verifyService.getVerifyStatus().intValue() == 1){
                page = resourceService.findByStatusAndCategoryIsInAndVerifyStatus(userType,category,1,pageNum,pageSize);

            }else {
                page = resourceService.findByStatusAndCategoryIsIn(userType, category, pageNum, pageSize);
            }
        }

        if( null != page){
            List<ResourceModel> resourceModelList = page.getContent();
            if(CollectionUtils.isNotEmpty(resourceModelList)){
                resourceModelList.forEach(resourceModel -> {
                    Paint paint = new Paint();
                    paint.setCategory(resourceModel.getCategory());
                    paint.setId(resourceModel.getId());
                    paint.setPngUrl(resourceUrl+resourceModel.getIcon());
                    paint.setZipUrl(resourceUrl+resourceModel.getResourceUrl());
                    paint.setTagKey(resourceModel.getTag());
                    paint.setType(resourceModel.getType());
                    paint.setStatus(resourceModel.getStatus());
                    paint.setCreateTime(resourceModel.getCreateTime());
                    paint.setType(resourceModel.getType());
                    paint.setShowTime(resourceModel.getShowTime());
                    paintList.add(paint);
                });
            }
        }

        return paintList;
    }

    private List<EveryDay> getDayList(Long time){
        List<EveryDay> everyDayList = new ArrayList<>();
        List<ResourceModel> resourceModelList = resourceService.findAllByShowTime(time);
        if(CollectionUtils.isNotEmpty(resourceModelList)){
            Map<Long,List<Paint>> paintMap = new HashMap<>();
            resourceModelList.forEach(resourceModel -> {
                if( null != resourceModel.getShowTime()){
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date(resourceModel.getShowTime()));
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.DAY_OF_MONTH,1);
                    Long dom = calendar.getTimeInMillis();
                    List<Paint> paints = paintMap.get(dom);
                    if(CollectionUtils.isEmpty(paints)){
                        paints = new ArrayList<>();
                    }
                    Paint paint = new Paint();
                    paint.setCategory(resourceModel.getCategory());
                    paint.setId(resourceModel.getId());
                    paint.setPngUrl(resourceUrl+resourceModel.getIcon());
                    paint.setZipUrl(resourceUrl+resourceModel.getResourceUrl());
                    paint.setTagKey(resourceModel.getTag());
                    paint.setShowTime(resourceModel.getShowTime());
                    paints.add(paint);
                    paintMap.put(dom,paints);
                }
            });

            List<Long> dayList = new ArrayList<>();
            paintMap.forEach((key,value)-> dayList.add(key));
            dayList.sort(Comparator.reverseOrder());

            log.info(JSON.toJSONString(dayList));
            if(MapUtils.isNotEmpty(paintMap)){
                dayList.forEach(key->{
                    EveryDay everyDay = new EveryDay();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date(key));
//                    everyDay.setMonth(calendar.get(Calendar.MONTH));
//                    everyDay.setPaintList(paintMap.get(key));
                    everyDayList.add(everyDay);

                });

//                dayList.sort((d1,d2)->new Long(d2-d1).intValue());
//                dayList.forEach(day->{
//                    EveryDay everyDay = new EveryDay();
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTime(new Date(day));
//                    int month = calendar.get(Calendar.MONTH);
//                    everyDay.setPaintList(paintMap.get(day));
//                    everyDay.setMonth(month+1);
//                    everyDayList.add(everyDay);
//                });

            }
        }

        return everyDayList;
    }

    private Paint getTodayPaint(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND,0);
        ResourceModel resourceModel = resourceService.findByShowTime(calendar.getTimeInMillis());
        if( null != resourceModel) {
            Paint paint = new Paint();
            paint.setCategory(resourceModel.getCategory());
            paint.setId(resourceModel.getId());
            paint.setPngUrl(resourceUrl + resourceModel.getIcon());
            paint.setZipUrl(resourceUrl + resourceModel.getResourceUrl());
            paint.setTagKey(resourceModel.getTag());
            paint.setShowTime(resourceModel.getShowTime());
            return paint;
        }else{
            return null;
        }
    }

    private Paint getTomorrow(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND,0);
        ResourceModel resourceModel = resourceService.findByShowTime(calendar.getTimeInMillis());
        if( null != resourceModel) {
            Paint paint = new Paint();
            paint.setCategory(resourceModel.getCategory());
            paint.setId(resourceModel.getId());
            paint.setPngUrl(resourceUrl + resourceModel.getIcon());
            paint.setZipUrl(resourceUrl + resourceModel.getResourceUrl());
            paint.setTagKey(resourceModel.getTag());
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(new Date(resourceModel.getShowTime()));
            int dom = calendar1.get(Calendar.DAY_OF_MONTH);
            paint.setShowTime(resourceModel.getShowTime());
            return paint;
        }else{
            return null;
        }
    }


    public EveryDayList getEveryDayList(Long time){
        EveryDayList everyDayList = new EveryDayList();
        everyDayList.setToday(this.getTodayPaint());
        everyDayList.setTomorrow(this.getTomorrow());
        everyDayList.setEveryDayList(this.getDayList(time));
        return everyDayList;
    }

    public EveryDay getEveryDay(String strategyId , String lastId){
        EveryDay everyDay = new EveryDay();
        String resourceId1 = recommendListService.getCacheNext(strategyId,lastId,1);
        String resourceId2 = recommendListService.getCacheNext(strategyId,lastId,2);
        RecommendListModel recommendListModel =  recommendListService.cacheGetRecommend();
        if( !StringUtils.isEmpty(resourceId1)){
            ResourceModel resourceModel1 =  resourceService.findById(resourceId1);
            if( null != resourceModel1 ) {
                Paint paint1 = new Paint();
                paint1.setCategory(resourceModel1.getCategory());
                paint1.setId(resourceModel1.getId());
                paint1.setPngUrl(resourceUrl + resourceModel1.getIcon());
                paint1.setZipUrl(resourceUrl + resourceModel1.getResourceUrl());
                paint1.setTagKey(resourceModel1.getTag());
                paint1.setShowTime(resourceModel1.getShowTime());
                everyDay.setCurrent(paint1);
            }
        }
        if( !StringUtils.isEmpty(resourceId2)){
            ResourceModel resourceModel2 =  resourceService.findById(resourceId2);
            if( null != resourceModel2) {
                Paint paint2 = new Paint();
                paint2.setCategory(resourceModel2.getCategory());
                paint2.setId(resourceModel2.getId());
                paint2.setPngUrl(resourceUrl + resourceModel2.getIcon());
                paint2.setZipUrl(resourceUrl + resourceModel2.getResourceUrl());
                paint2.setTagKey(resourceModel2.getTag());
                paint2.setShowTime(resourceModel2.getShowTime());
                everyDay.setNext(paint2);
            }
        }

        if( null != recommendListModel) {
            everyDay.setStrategyId(recommendListModel.getId());
            everyDay.setTimeInterval(recommendListModel.getIntervalTime());
        }

        return everyDay;
    }


    public Paint getById(String id){
        Paint paint = new Paint();
        ResourceModel resourceModel = resourceService.findById(id);
        if( null != resourceModel){
            paint.setCategory(resourceModel.getCategory());
            paint.setId(resourceModel.getId());
            paint.setPngUrl(resourceUrl + resourceModel.getIcon());
            paint.setZipUrl(resourceUrl + resourceModel.getResourceUrl());
            paint.setTagKey(resourceModel.getTag());
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(new Date(resourceModel.getShowTime()));
            int dom = calendar1.get(Calendar.DAY_OF_MONTH);
            paint.setShowTime(resourceModel.getShowTime());
        }

        return paint;
    }


    public static void main(String[] args){
        List<Long> dayList = new ArrayList<>();
        dayList.add(1572537600000L);
        dayList.add(1559318400000L);
        dayList.add(1567267200000L);
        dayList.add(1575129600000L);
        dayList.add(1561910400000L);
        dayList.add(1569859200000L);
        dayList.add(1564588800000L);
        dayList.sort(Comparator.reverseOrder());

       // Collections.sort(dayList);
       // Collections.reverse(dayList);
        dayList.forEach(a -> {
            System.out.println(a);
        });
    }
}
