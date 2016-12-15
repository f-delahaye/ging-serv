package org.gingolph.gingserv;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class GingServer  extends WebMvcConfigurerAdapter {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    	converters.add(new TopicMapJsonConverter());
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
// https://spring.io/blog/2013/12/19/serving-static-web-content-with-spring-boot      
        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**").addResourceLocations("classpath:/public/");
        }
    }    
    
    public static void main(String[] args) {
        SpringApplication.run(GingServer.class, args);
    }
}