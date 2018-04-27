package cn.tonghao.remex.common.util.security;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by howetong on 2018/2/8.
 */
public class MD5UtilsTest {

    @Test
    public void md5Sign() throws Exception{
        String str = "hello,world!";
        System.out.println(MD5Utils.md5Direct(str, null));
        System.out.println(MD5Utils.md5Direct(str));
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(now.format(formatter));
    }

}