package com.onyouxi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.unit.DataSize;
import org.springframework.web.client.RestTemplate;

import javax.servlet.MultipartConfigElement;
import java.util.Arrays;

/**
 * Created by jack on 15/5/12.
 */
@EnableScheduling
@SpringBootApplication
public class Application  {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    // Do any additional configuration here
    RestTemplate restTemplate = builder.build();
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

    converter.setSupportedMediaTypes(
            Arrays.asList(new MediaType[]{MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM}));

    restTemplate.setMessageConverters(Arrays.asList(converter, new FormHttpMessageConverter()));
    return restTemplate;

  }

  @Bean
  public MultipartConfigElement multipartConfig() {
    MultipartConfigFactory factory = new MultipartConfigFactory();
    factory.setMaxFileSize(DataSize.ofMegabytes(5));
    factory.setMaxRequestSize(DataSize.ofMegabytes(5));
    return factory.createMultipartConfig();
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Application.class, args);
  }
}
