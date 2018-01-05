package cn.tonghao.remex.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;

/**
 * Created by howetong on 2018/1/5.
 */
public class JsonHttpMessageConverter extends MappingJackson2HttpMessageConverter {
    private static final Logger log = LoggerFactory.getLogger(JsonHttpMessageConverter.class);

    public JsonHttpMessageConverter() {
    }

    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        try {
            byte[] bytes = this.getObjectMapper().writeValueAsBytes(object);
            FileCopyUtils.copy(bytes, outputMessage.getBody());
        } catch (JsonProcessingException var4) {
            log.error("Could not write JSON: " + var4.getMessage(), var4);
            throw new HttpMessageNotWritableException("Could not write JSON: " + var4.getMessage(), var4);
        }
    }
}
