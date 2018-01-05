package cn.tonghao.remex.common.annotation;

import java.lang.annotation.*;

/**
 * Created by howetong on 2018/1/5.
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseJson {
    boolean translate() default false;

    ResponseJson.Location location() default ResponseJson.Location.BYREQUEST;

    ResponseJson.CompressType compressType() default ResponseJson.CompressType.NOCOMPRESS;

    public static enum CompressType {
        NOCOMPRESS,
        SNAPPY,
        GZIP,
        BYREQUEST;

        private CompressType() {
        }
    }

    public static enum Location {
        UNDEFINED,
        DATA,
        MESSAGE,
        BYREQUEST;

        private Location() {
        }
    }
}
