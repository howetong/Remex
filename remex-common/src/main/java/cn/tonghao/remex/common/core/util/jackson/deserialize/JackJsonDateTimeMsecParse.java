package cn.tonghao.remex.common.core.util.jackson.deserialize;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JackJsonDateTimeMsecParse extends JsonDeserializer<Date> {
    private static final Logger LOG = LoggerFactory.getLogger(JackJsonDateTimeMsecParse.class);

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String date = jp.getText();
        if (date == null || date.trim().length() == 0) {
            return null;
        }
        try {
            return format.parse(date);
        } catch (Exception e) {
            LOG.error("日期解析出错", e);
        }
        return null;
    }

}
