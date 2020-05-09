package com.onyouxi.task;

import com.onyouxi.service.RecommendListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RecommendListCacheTask {

    @Autowired
    private RecommendListService recommendListService;


    @Scheduled(cron = "0 0/5 * * * ?")
    public void initRecommendList(){
        recommendListService.reloadCache();
    }
}
