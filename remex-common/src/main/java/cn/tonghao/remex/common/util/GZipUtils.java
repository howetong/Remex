package cn.tonghao.remex.common.util;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by howetong on 2018/1/5.
 */
public class GZipUtils implements IClientCompressUtils {
    public static final int BUFFER = 1024;
    public static final String EXT = ".gz";

    public GZipUtils() {
    }

    public byte[] compress(byte[] data) {
        byte[] output = null;

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            compress(bais, baos);
            output = baos.toByteArray();
            baos.flush();
            baos.close();
            bais.close();
        } catch (Exception var5) {
            ;
        }

        return output;
    }

    public String compress(File file) throws Exception {
        return this.compress(file, false);
    }

    public String compress(File file, boolean delete) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file.getPath() + ".gz");
        compress(fis, fos);
        fis.close();
        fos.flush();
        fos.close();
        if(delete) {
            file.delete();
        }

        return file.getPath() + ".gz";
    }

    public static void compress(InputStream is, OutputStream os) throws Exception {
        GZIPOutputStream gos = new GZIPOutputStream(os);
        byte[] data = new byte[1024];

        int count;
        while((count = is.read(data, 0, 1024)) != -1) {
            gos.write(data, 0, count);
        }

        gos.finish();
        gos.flush();
        gos.close();
    }

    public String compress(String path) throws Exception {
        return this.compress(path, false);
    }

    public String compress(String path, boolean delete) throws Exception {
        File file = new File(path);
        return this.compress(file, delete);
    }

    public byte[] decompress(byte[] data) {
        byte[] out = null;

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            decompress(bais, baos);
            out = baos.toByteArray();
            baos.flush();
            baos.close();
            bais.close();
        } catch (Exception var5) {
            ;
        }

        return out;
    }

    public String decompress(File file) throws Exception {
        return this.decompress(file, false);
    }

    public String decompress(File file, boolean delete) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file.getPath().replace(".gz", ""));
        decompress(fis, fos);
        fis.close();
        fos.flush();
        fos.close();
        if(delete) {
            file.delete();
        }

        return file.getPath().replace(".gz", "");
    }

    public static void decompress(InputStream is, OutputStream os) throws Exception {
        GZIPInputStream gis = new GZIPInputStream(is);
        byte[] data = new byte[1024];

        int count;
        while((count = gis.read(data, 0, 1024)) != -1) {
            os.write(data, 0, count);
        }

        gis.close();
    }

    public String decompress(String path) throws Exception {
        return this.decompress(path, false);
    }

    public String decompress(String path, boolean delete) throws Exception {
        File file = new File(path);
        return this.decompress(file, delete);
    }
}
