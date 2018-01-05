package cn.tonghao.remex.common.util;

import java.io.File;

/**
 * Created by howetong on 2018/1/5.
 */
public interface IClientCompressUtils {
    byte[] compress(byte[] var1);

    byte[] decompress(byte[] var1);

    String compress(File var1) throws Exception;

    String compress(File var1, boolean var2) throws Exception;

    String compress(String var1) throws Exception;

    String compress(String var1, boolean var2) throws Exception;

    String decompress(String var1) throws Exception;

    String decompress(String var1, boolean var2) throws Exception;

    String decompress(File var1) throws Exception;

    String decompress(File var1, boolean var2) throws Exception;
}