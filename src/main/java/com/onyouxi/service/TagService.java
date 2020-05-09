package com.onyouxi.service;

import com.onyouxi.constant.Const;
import com.onyouxi.model.dbModel.TagModel;
import com.onyouxi.repository.manager.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    /**
     * 保存一个标签
     * @param tag
     * @return
     */
    public Integer saveTag(TagModel tag){
        TagModel result = tagRepository.findByKeyValue(tag.getKeyValue());
        if( null != result){
            return 1;
        }
        tag.setCreateTime(System.currentTimeMillis());
        tag.setStatus(Const.STATUS_INIT);
        tagRepository.save(tag);

        return 0;
    }

    public Page<TagModel> findByType(int type , int page, int size){
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = PageRequest.of(page, size,sort);
        return tagRepository.findByType(type,pageRequest);
    }

    public Page<TagModel> findByTypeAndIdNotIn(int type , List<String> idList, int page, int size){
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = PageRequest.of(page, size,sort);
        return tagRepository.findByTypeAndIdNotIn(type,idList,pageRequest);
    }



    public TagModel findById(String id){
        Optional<TagModel> optionalTagModel = tagRepository.findById(id);
        return optionalTagModel.get();
    }


    public Integer updateTag(TagModel tag){
        TagModel tagModel = tagRepository.findByKeyValueAndIdIsNot(tag.getKeyValue(),tag.getId());
        if( null != tagModel){
            return 1;
        }
        tagModel = tagRepository.findById(tag.getId()).get();
        if(StringUtils.isNotEmpty(tag.getColor())) {
            tagModel.setColor(tag.getColor());
        }
        if(MapUtils.isNotEmpty(tag.getTagMap())) {
            tagModel.setTagMap(tag.getTagMap());
        }
        if( null != tag.getStatus()){
            tagModel.setStatus( tag.getStatus());
        }
        if( null != tag.getSort()){
            tagModel.setSort(tag.getSort());
        }
        if( null != tag.getKeyValue()){
            tagModel.setKeyValue(tag.getKeyValue());
        }
        if( null != tag.getTextColor()){
            tagModel.setTextColor(tag.getTextColor());
        }
        tagRepository.save(tagModel);
        return 0;
    }



    public void delTag(String id){
        TagModel tagModel = new TagModel();
        tagModel.setId(id);
        tagRepository.delete(tagModel);
    }

    public List<TagModel> findAll(){
       return tagRepository.findByStatus(Const.STATUS_NORMAL);
    }


}
