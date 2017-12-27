package cn.tonghao.remex.common.core.util.jackson.deserialize;




import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class JackJsonBigDecimalParse extends JsonDeserializer<BigDecimal> {
    private static final Logger LOG = LoggerFactory.getLogger(JackJsonBigDecimalParse.class);

    @Override
    public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        String value = jp.getText();
        if (value == null || value.trim().length() == 0) {
            return null;
        }
        try {
            BigDecimal result = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
            return result;
        } catch (Exception e) {
            LOG.error("数字解析出错", e);
        }
        return null;
    }

}
