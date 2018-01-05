package cn.tonghao.remex.common.util;

import org.xerial.snappy.Snappy;
import org.xerial.snappy.SnappyInputStream;
import org.xerial.snappy.SnappyOutputStream;

import java.io.*;

/**
 * Created by howetong on 2018/1/5.
 */
public class SnappyUtils implements IClientCompressUtils {
    public static final String EXT = ".snappy";
    public static final int BUFFER = 1024;

    public SnappyUtils() {
    }

    public byte[] compress(byte[] data) {
        byte[] output = null;

        try {
            byte[] testput = new byte[Snappy.maxCompressedLength(data.length)];
            int javaCompressedSize = Snappy.compress(data, 0, data.length, testput, 0);
            if(javaCompressedSize > 0) {
                output = new byte[javaCompressedSize];
                System.arraycopy(testput, 0, output, 0, javaCompressedSize);
            }
        } catch (IOException var5) {
            ;
        }

        return output;
    }

    public byte[] decompress(byte[] data) {
        byte[] output = null;

        try {
            int size = data.length * 3;
            output = new byte[size];
            Snappy.uncompress(data, 0, data.length, output, 0);
        } catch (IOException var5) {
            ;
        }

        return output;
    }

    public String compress(File file) throws Exception {
        return this.compress(file, true);
    }

    public String compress(String path) throws Exception {
        return this.compress(path, false);
    }

    public String decompress(String path) throws Exception {
        return this.decompress(path, false);
    }

    public String decompress(File file) throws Exception {
        return this.decompress(file, false);
    }

    public String compress(String path, boolean delete) throws Exception {
        File file = new File(path);
        return this.compress(file, delete);
    }

    public String compress(File file, boolean delete) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file.getPath() + ".snappy");
        compress(fis, fos);
        fis.close();
        fos.flush();
        fos.close();
        if(delete) {
            file.delete();
        }

        return file.getPath() + ".snappy";
    }

    public static void compress(InputStream is, OutputStream os) throws Exception {
        SnappyOutputStream gos = new SnappyOutputStream(os);
        byte[] data = new byte[1024];

        int count;
        while((count = is.read(data, 0, 1024)) != -1) {
            gos.write(data, 0, count);
        }

        gos.flush();
        gos.close();
    }

    public String decompress(File file, boolean delete) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file.getPath().replace(".snappy", ""));
        decompress(fis, fos);
        fis.close();
        fos.flush();
        fos.close();
        if(delete) {
            file.delete();
        }

        return file.getPath().replace(".snappy", "");
    }

    public static void decompress(InputStream is, OutputStream os) throws Exception {
        SnappyInputStream gis = new SnappyInputStream(is);
        byte[] data = new byte[1024];

        int count;
        while((count = gis.read(data, 0, 1024)) != -1) {
            os.write(data, 0, count);
        }

        gis.close();
    }

    public String decompress(String path, boolean delete) throws Exception {
        File file = new File(path);
        return this.decompress(file, delete);
    }
}
