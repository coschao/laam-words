package com.laam.words.cmn.conf;

import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
    
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.setPatternParser(null);
	}

//    @Override
//    public void configureViewResolvers(ViewResolverRegistry registry) {
//        // application.yml에서 타임리프 설정 제어(자동) // 스프링 부트에서는 spring-boot-starter-thymeleaf 만 있으면 자동으로 설정
////    	# application.properties 예시
////    	spring.thymeleaf.prefix=classpath:/templates/
////    	spring.thymeleaf.suffix=.html
////    	spring.thymeleaf.cache=false
//    }

    /**
     * 정적 파일(이미지, CSS, JavaScript 등)을 어디에서 제공할지 설정
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**") // 요청 경로
                .addResourceLocations("classpath:/static/");  // 정적 파일 위치
    }

}
