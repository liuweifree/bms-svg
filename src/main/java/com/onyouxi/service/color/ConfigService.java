package com.onyouxi.service.color;

import com.onyouxi.constant.Const;
import com.onyouxi.model.dbModel.ConfigModel;
import com.onyouxi.model.dbModel.TagModel;
import com.onyouxi.model.view.*;
import com.onyouxi.service.ConfigManageService;
import com.onyouxi.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigService {

    @Autowired
    private TagService tagService;

    @Autowired
    private ConfigManageService configManageService;


    public Config getConfig(String lanquage){
        List<TagModel> tagModelList = tagService.findAll();
        Config config = new Config();
        if(!CollectionUtils.isEmpty(tagModelList)){
            List<Category> categoryList = new ArrayList<>();
            List<Tag> tagList = new ArrayList<>();
            tagModelList.forEach(tagModel -> {
                if(tagModel.getType() == 1){
                    Category category = new Category();
                    category.setKey(tagModel.getKeyValue());
                    category.setName(tagModel.getTagMap().get(lanquage));
                    category.setSort(tagModel.getSort());
                    categoryList.add(category);
                }else{
                    Tag tag = new Tag();
                    tag.setKey(tagModel.getKeyValue());
                    tag.setName(tagModel.getTagMap().get(lanquage));
                    tag.setBackgroundColor(tagModel.getColor());
                    tag.setTextColor(tagModel.getTextColor());
                    tagList.add(tag);
                }

            });

            config.setCategoryList(categoryList);
            config.setTagList(tagList);
        }

        List<ConfigModel> configModelList = configManageService.findAll();
        if(!CollectionUtils.isEmpty(configModelList)){
            List<Ad> adList = new ArrayList<>();
            List<Product> productList = new ArrayList<>();
            List<SwitchModel> switchList = new ArrayList<>();
            configModelList.forEach(configModel -> {
                if(configModel.getType()==0){
                    if(!CollectionUtils.isEmpty(configModel.getParamList())) {
                        configModel.getParamList().forEach(paramModel -> {
                            Ad ad = new Ad();
                            ad.setAdId(paramModel.getPid());
                            ad.setPosition(paramModel.getPosition());
                            adList.add(ad);
                        });
                        config.setAdList(adList);
                    }
                }else if(configModel.getType()==1){
                    if(!CollectionUtils.isEmpty(configModel.getParamList())) {
                        configModel.getParamList().forEach(paramModel -> {
                            Product product = new Product();
                            product.setProductId(paramModel.getPid());
                            product.setDiscount(paramModel.getDiscount());
                            productList.add(product);
                        });
                        config.setProductList(productList);
                    }
                }else if(configModel.getType()==2){
                    if(!CollectionUtils.isEmpty(configModel.getParamList())) {
                        configModel.getParamList().forEach(paramModel -> {
                            SwitchModel switchModel = new SwitchModel();
                            switchModel.setKey(paramModel.getPid());
                            switchModel.setValue(Integer.valueOf(paramModel.getPosition()));
                            switchList.add(switchModel);
                        });
                        config.setSwitchModelList(switchList);
                    }
                }

            });
        }

        return config;

    }
}
