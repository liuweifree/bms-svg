package com.onyouxi.model.view;

import lombok.Data;

import java.util.List;

@Data
public class Config {

    private List<Category> categoryList;

    private List<Tag> tagList;

    private List<Ad> adList;

    private List<Product> productList;

    private List<SwitchModel> switchModelList;


}
