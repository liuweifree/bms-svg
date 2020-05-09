package com.onyouxi.model.dbModel;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "image_background")
public class ImageBgModel {

    private String id;

    private String imageUrl;

    private String name;

    private Long createTime;
}
