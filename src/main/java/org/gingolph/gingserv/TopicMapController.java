package org.gingolph.gingserv;

import org.gingolph.gingmap.TopicMapImpl;
import org.gingolph.gingmap.json.JsonTopicMapSystemFactory;
import org.gingolph.gingmap.json.TopicMapSupportJson;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

import mjson.Json;

@RestController
public class TopicMapController {
	static final TopicMapSystem jsonTopicMapSystem = initialiseTopicMapSystem();

	private static TopicMapSystem initialiseTopicMapSystem() {
		try {
			return new JsonTopicMapSystemFactory().newTopicMapSystem();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

    @RequestMapping(value="/topicmap", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public TopicMap view(@RequestParam(value="user", defaultValue="anonymous") String user) throws Exception {
		TopicMap topicMap = jsonTopicMapSystem.getTopicMap(user);
		if (topicMap == null) {
			topicMap = jsonTopicMapSystem.createTopicMap(user);
		}
		topicMap.createTopicByItemIdentifier(topicMap.createLocator("France"));
		return topicMap;
    }

}
