package com.onyouxi.model.dbModel;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "template")
public class TemplateModel {

    private String id;

    private String name;

    private String content;

    private Long createTime;
}
