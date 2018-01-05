package cn.tonghao.remex.util;

import cn.tonghao.remex.business.core.util.SerialNoGenUtils;
import org.junit.Test;

/**
 * unit test
 * Created by howetong on 2018/1/5.
 */
public class SerialNoGenUtilsTest {

    @Test
    public void geneOutTradNo() throws Exception {
        String outNo = SerialNoGenUtils.geneOutTradNo(1001,1);
        System.out.println(outNo);
    }

}