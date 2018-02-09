package cn.tonghao.remex.common.util.security;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by howetong on 2018/2/8.
 */
public class MD5SignatureTest {

    @Test
    public void md5Sign() throws Exception{
        String str = "hello,world!";
        System.out.println(MD5Signature.md5Direct(str, null));
        System.out.println(MD5Signature.md5Direct(str));
        System.out.println(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
    }

}