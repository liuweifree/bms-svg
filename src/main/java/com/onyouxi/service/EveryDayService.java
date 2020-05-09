package com.onyouxi.service;

import com.onyouxi.model.dbModel.EveryDayModel;
import com.onyouxi.repository.manager.EveryDayRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EveryDayService {

    @Autowired
    private EveryDayRepository everyDayRepository;


    public List<EveryDayModel> getOneYear(){
        List<EveryDayModel> everyDayModelList = new ArrayList<>();


        return everyDayModelList;
    }

    public void save(EveryDayModel everyDayModel){
        everyDayRepository.save(everyDayModel);
    }

    public Page<EveryDayModel> findAll( int page,int size){
        Sort sort = Sort.by(Sort.Direction.DESC, "month");
        PageRequest pageRequest = PageRequest.of(page, size,sort);
        return everyDayRepository.findAll(pageRequest);
    }

    public void del(String id){
        everyDayRepository.deleteById(id);
    }
}
