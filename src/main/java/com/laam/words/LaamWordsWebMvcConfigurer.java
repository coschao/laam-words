package com.laam.words;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LaamWordsWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //registry.addViewController("/").setViewName("index"); // 루트 경로로 index.html(또는 템플릿) 매핑
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 커스텀 리소스 경로 추가 등
    }
}
