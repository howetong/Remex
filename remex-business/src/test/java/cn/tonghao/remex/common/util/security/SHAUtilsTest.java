package cn.tonghao.remex.common.util.security;

import org.junit.Test;

/**
 * Created by howetong on 2018/3/21.
 */
public class SHAUtilsTest {
    @Test
    public void sha256Encrypt() throws Exception{
        String str = "6214830257531607";
        System.out.println(SHAUtils.SHA256Encrypt(str));
    }
}