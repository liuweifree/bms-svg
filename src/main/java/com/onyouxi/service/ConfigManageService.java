package com.onyouxi.service;

import com.onyouxi.model.dbModel.ConfigModel;
import com.onyouxi.repository.manager.ConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ConfigManageService {

    @Autowired
    private ConfigRepository configRepository;

    public ConfigModel findByType(Integer type){
        //this.delByType(type);
        return configRepository.findByType(type);
    }

    public ConfigModel updateOrSaveConfig(ConfigModel configModel){
        ConfigModel cfm = configRepository.save(configModel);
        return cfm;
    }

    public void delByType(Integer type){
        configRepository.deleteByType(type);
    }

    public List<ConfigModel> findAll(){
        return configRepository.findAll();
    }

}
