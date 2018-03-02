package cn.tonghao.remex.controller.pay.api;

import cn.tonghao.remex.business.core.annotation.BeanValid;
import cn.tonghao.remex.business.core.log.RemexLogger;
import cn.tonghao.remex.business.core.util.ResponseUtil;
import cn.tonghao.remex.business.pay.dto.cashier.CashierDetailLayoutReqDTO;
import cn.tonghao.remex.business.pay.enums.StandardResponseCode;
import cn.tonghao.remex.common.annotation.Json;
import cn.tonghao.remex.common.annotation.ResponseJson;
import cn.tonghao.remex.common.util.ConvertUtil;
import cn.tonghao.remex.common.util.JsonUtil;
import cn.tonghao.remex.common.util.ParameterUtil;
import cn.tonghao.remex.common.util.Response;
import cn.tonghao.remex.common.util.security.MD5Utils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * Created by howetong on 2018/1/5.
 */
@Controller
@RequestMapping("/cashier")
public class CashierController {

    private static final Logger logger = RemexLogger.getLogger(CashierController.class);

    @Value("${pay.MD5KEY}")
    private String MD5KEY;

    /**
     * 具体布局接口，含订单信息。不同订单的收银台布局不同
     */
    @RequestMapping(value = "/layout/detail")
    @ResponseJson
    @BeanValid
    public Response getDetailLayout(@Json CashierDetailLayoutReqDTO layoutReqDTO) throws IOException {
        logger.info("【布局接口日志】布局接口入参:{}", JsonUtil.toString(layoutReqDTO));
        try {
            //验签
            String content = ParameterUtil.getSignDataIgnoreNull(ConvertUtil.bean2Map(layoutReqDTO));
            if (!MD5Utils.validate(content, layoutReqDTO.getSign(), MD5KEY)) {
                logger.info("布局接口非法签名");
                return ResponseUtil.genResponse(StandardResponseCode.SYS_INVALID_SIGNATURE);
            }
            return ResponseUtil.genResponse(StandardResponseCode.SUCCESS);
        } catch (Exception e) {
            logger.error("获取具体布局出错,原因是:{}", e.getMessage());
            return ResponseUtil.genResponse(StandardResponseCode.SYS_INTERNAL_ERROR);
        }

    }
}
