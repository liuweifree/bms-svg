package com.onyouxi.model.dbModel;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;


public class EveryDayModel {

    private Date month;

    private List<ResourceModel> resourceModelList;

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public List<ResourceModel> getResourceModelList() {
        return resourceModelList;
    }

    public void setResourceModelList(List<ResourceModel> resourceModelList) {
        this.resourceModelList = resourceModelList;
    }
}
