package cn.tonghao.remex.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.codec.binary.Base64;
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
public class CompressHttpMessageConverter extends MappingJackson2HttpMessageConverter {
    private static final Logger log = LoggerFactory.getLogger(CompressHttpMessageConverter.class);
    private int converterType;
    private int compressType;

    public CompressHttpMessageConverter() {
        this.initialConverter();
    }

    public CompressHttpMessageConverter(int converterType, int compressType) {
        this.converterType = converterType;
        this.compressType = compressType;
        this.initialConverter();
    }

    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        try {
            byte[] bytes = this.getObjectMapper().writeValueAsBytes(object);
            byte[] lastBytes = null;
            IClientCompressUtils iClientCompressUtils = null;
            switch(this.converterType) {
                case 2:
                    break;
                case 3:
                    bytes = Base64.encodeBase64(bytes);
                    break;
                default:
                    bytes = Base64.encodeBase64(bytes);
            }

            switch(this.compressType) {
                case 0:
                    lastBytes = bytes;
                    break;
                case 1:
                    iClientCompressUtils = ClientCompressFactory.getCompressUtils(1);
                    lastBytes = iClientCompressUtils.compress(bytes);
                    outputMessage.getHeaders().set("TN-DATA-COMPRESS-TYPE", "T-GZIP");
                    break;
                case 2:
                    iClientCompressUtils = ClientCompressFactory.getCompressUtils(2);
                    lastBytes = iClientCompressUtils.compress(bytes);
                    outputMessage.getHeaders().set("TN-DATA-COMPRESS-TYPE", "T-SNAPPY");
                    break;
                default:
                    lastBytes = bytes;
            }

            FileCopyUtils.copy(lastBytes, outputMessage.getBody());
        } catch (JsonProcessingException var6) {
            log.error("Could not write JSON: " + var6.getMessage(), var6);
            throw new HttpMessageNotWritableException("Could not write JSON: " + var6.getMessage(), var6);
        }
    }

    private void initialConverter() {
    }
}