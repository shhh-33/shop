package com.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 업로드한 파일을 읽어올 경로 설정
 * addResourceHandler를 통해서 자신의 로컬 컴퓨터에 업로드한 파일을 찾을 위치 설정
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}") //properties에 설정한 uploadPath 프로퍼티 값 읽어옴
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**") //uploadPath에 설정한 폴더를 기준으로 파일 읽어옴
                .addResourceLocations(uploadPath); //로컬 컴퓨터에 저장된 파일을 읽어올 root 경로 설정
    }

}