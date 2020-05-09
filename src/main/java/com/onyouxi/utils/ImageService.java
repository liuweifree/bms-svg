package com.onyouxi.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ImageService {

    public void changeSize(String fileName,Integer width,Integer height,String outFile){
        try {
            Thumbnails.of(fileName).size(width,height).toFile(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeQuality(String fileName,String outFile,float quality){
        try {
            Thumbnails.of(fileName).size(320,320).outputQuality(quality).toFile(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
