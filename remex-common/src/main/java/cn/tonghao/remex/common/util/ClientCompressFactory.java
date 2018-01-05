package cn.tonghao.remex.common.util;

/**
 * Created by howetong on 2018/1/5.
 */
public class ClientCompressFactory {
    public static final int COMPRESS_GZIP = 1;
    public static final int COMPRESS_SNAPPY = 2;
    public static final int COMPRESS_UNKNOW = 0;
    public static final int COMPRESS_DEFAULT = 2;
    public static final String HTTP_HEAD_REQUEST_COMPRESSTYPE = "TN-REQ-COMPRESS-TYPE";
    public static final String HTTP_HEAD_DATA_COMPRESSTYPE = "TN-DATA-COMPRESS-TYPE";
    public static final String COMPRESS_NAME_SNAPPY = "T-SNAPPY";
    public static final String COMPRESS_NAME_GZIP = "T-GZIP";

    public ClientCompressFactory() {
    }

    public static IClientCompressUtils getCompressUtils(int compressType) {
        IClientCompressUtils iClientCompressUtils = null;
        switch(compressType) {
            case 1:
                iClientCompressUtils = new GZipUtils();
                break;
            case 2:
                iClientCompressUtils = new SnappyUtils();
                break;
            default:
                iClientCompressUtils = new SnappyUtils();
        }

        return (IClientCompressUtils)iClientCompressUtils;
    }

    public static IClientCompressUtils getCompressUtils() {
        return getCompressUtils(2);
    }

    public static int getCompressTypeFromHeanderString(String header) {
        int type = 0;
        if(header != null || header.isEmpty()) {
            if(header.compareToIgnoreCase("T-SNAPPY") == 0) {
                type = 2;
            } else if(header.compareToIgnoreCase("T-GZIP") == 0) {
                type = 1;
            }
        }

        return type;
    }
}
