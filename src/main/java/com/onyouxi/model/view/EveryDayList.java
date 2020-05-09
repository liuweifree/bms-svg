package com.onyouxi.model.view;

import lombok.Data;

import java.util.List;

@Data
public class EveryDayList {

    private Paint today;

    private Paint tomorrow;

    private List<EveryDay> everyDayList;
}
