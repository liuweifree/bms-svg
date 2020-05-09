package com.onyouxi.service;

import com.onyouxi.model.dbModel.TemplateModel;
import com.onyouxi.repository.manager.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService {

    @Autowired
    private TemplateRepository templateRepository;


    public Page<TemplateModel> findAll(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return templateRepository.findAll(pageRequest);
    }

    public void save(TemplateModel templateModel){
        templateModel.setCreateTime(System.currentTimeMillis());
        templateRepository.save(templateModel);
    }

    public void delete(String id){
        templateRepository.deleteById(id);
    }

    public List<TemplateModel> findAll(){
        return templateRepository.findAll();
    }
}
