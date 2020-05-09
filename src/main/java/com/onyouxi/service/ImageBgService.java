package com.onyouxi.service;

import com.onyouxi.model.dbModel.ImageBgModel;
import com.onyouxi.repository.manager.ImageBgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageBgService {

    @Autowired
    private ImageBgRepository imageBgRepository;


    public Page<ImageBgModel> findAll( int page,int size){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return imageBgRepository.findAll(pageRequest);
    }

    public void save(ImageBgModel imageBgModel){
        imageBgModel.setCreateTime(System.currentTimeMillis());
        imageBgRepository.save(imageBgModel);
    }

    public void delete(String id){
        imageBgRepository.deleteById(id);
    }

    public List<ImageBgModel> findAll(){
        return imageBgRepository.findAll();
    }

}
