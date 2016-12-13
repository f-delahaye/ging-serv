package org.gingolph.gingserv;

import java.util.List;

import org.gingolph.gingmap.TopicMapSystemImpl;
import org.gingolph.gingmap.json.JsonTopicMapSystemFactory;
import org.gingolph.gingserv.json.TopicMapJsonConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class GingServer  extends WebMvcConfigurerAdapter {

	static final TopicMapSystemImpl jsonTopicMapSystem = initialiseTopicMapSystem();

	private static TopicMapSystemImpl initialiseTopicMapSystem() {
		try {
			return new JsonTopicMapSystemFactory().newTopicMapSystem();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    	converters.add(new TopicMapJsonConverter(jsonTopicMapSystem));
    }
    
    public static void main(String[] args) {
        SpringApplication.run(GingServer.class, args);
    }
}