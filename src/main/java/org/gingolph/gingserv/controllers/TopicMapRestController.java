package org.gingolph.gingserv.controllers;

import org.gingolph.gingmap.hg.HGTopicMapSystemFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tmapi.core.TopicMap;
import org.tmapi.core.TopicMapSystem;

@RestController
public class TopicMapRestController {
	static final TopicMapSystem hgTopicMapSystem = initialiseTopicMapSystem();

	private static TopicMapSystem initialiseTopicMapSystem() {
		try {
			return new HGTopicMapSystemFactory().withStoragePath("hg-storage").withCloseOnExit(true).newTopicMapSystem();
//          return new IMTopicMapSystemFactory().newTopicMapSystem();		  
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

    @RequestMapping(value="/topicmap", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public TopicMap view(@RequestParam(value="user", required=true) String user) throws Exception {
		TopicMap topicMap = hgTopicMapSystem.getTopicMap(user);
		if (topicMap == null) {
			topicMap = hgTopicMapSystem.createTopicMap(user);
			// Create a basic topic maps per user
			topicMap.createTopicByItemIdentifier(topicMap.createLocator("Bookmarks"));
		}
		return topicMap;
    }
    
    @RequestMapping(value="/topicmap", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public TopicMap merge(@RequestBody(required=true) TopicMap topicMap) {
      TopicMap userTopicMap = hgTopicMapSystem.getTopicMap(topicMap.getLocator());
      userTopicMap.mergeIn(topicMap);
      return userTopicMap;
    }

}
