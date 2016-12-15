package org.gingolph.gingserv;

import java.io.IOException;
import java.nio.charset.Charset;

import org.gingolph.gingmap.LocatorImpl;
import org.gingolph.gingmap.TopicMapSystemImpl;
import org.gingolph.gingmap.json.JsonTopicMapSupport;
import org.gingolph.gingmap.json.JsonTopicMapSystemFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;
import org.tmapi.core.TopicMap;

public class TopicMapJsonConverter extends AbstractHttpMessageConverter<TopicMap> {


  final TopicMapSystemImpl jsonTopicMapSystem;

  public TopicMapJsonConverter() {
    super(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8);
    this.jsonTopicMapSystem = new JsonTopicMapSystemFactory().newTopicMapSystem();
  }

  @Override
  protected boolean supports(Class<?> clazz) {
    return TopicMap.class.isAssignableFrom(clazz);
  }

  @Override
  protected TopicMap readInternal(Class<? extends TopicMap> clazz, HttpInputMessage inputMessage)
      throws IOException, HttpMessageNotReadableException {
    String jsonAsString =
        StreamUtils.copyToString(inputMessage.getBody(), getCharset(inputMessage));
    return JsonTopicMapSupport.read(jsonAsString, jsonTopicMapSystem);
  }

  @Override
  protected void writeInternal(TopicMap t, HttpOutputMessage outputMessage)
      throws IOException, HttpMessageNotWritableException {
    JsonTopicMapSupport tmSupportJson = new JsonTopicMapSupport();
    TopicMap jsonTopicMap = jsonTopicMapSystem.createTopicMap(tmSupportJson);
    tmSupportJson.setBaseLocator((LocatorImpl) t.getLocator());
    jsonTopicMap.mergeIn(t);
    StreamUtils.copy(tmSupportJson.toString(), getCharset(outputMessage), outputMessage.getBody());
  }

  private Charset getCharset(HttpMessage message) {
    Charset charset = message.getHeaders().getContentType().getCharset();
    return charset == null ? Charset.defaultCharset() : charset;
  }


}
