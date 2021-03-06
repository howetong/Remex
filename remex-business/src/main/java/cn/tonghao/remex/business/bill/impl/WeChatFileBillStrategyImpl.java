package cn.tonghao.remex.business.bill.impl;

import cn.tonghao.remex.business.bill.CommonFileBillStrategy;
import cn.tonghao.remex.business.bill.IChannelFileBillStrategy;
import cn.tonghao.remex.business.core.log.RemexLogger;
import cn.tonghao.remex.business.pay.util.WechatPayUtil;
import cn.tonghao.remex.common.util.DateTimeUtil;
import cn.tonghao.remex.common.util.http.HttpClientUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 渠道对账单获取
 * Created by howetong on 2018/2/11.
 */
@Service("wechatFileBillStrategy")
public class WeChatFileBillStrategyImpl extends CommonFileBillStrategy implements IChannelFileBillStrategy {

    private static final Logger logger = RemexLogger.getLogger(WeChatFileBillStrategyImpl.class);

    public static final String channelName = "wechat"; //渠道名作为文件子目录

    @Value("${weChat.app.key}")
    private String WECHAT_APP_KEY;

    @Value("${wechat.api.bill.url}")
    private String billUrl;

    @Override
    public void getFillBill() {
        try {
            //当前日期前一天
            String date = DateTimeUtil.getDateBeforeDay("yyyyMMdd");
            //本地文件保存路径
            String filePath = System.getProperty("catalina.base") + File.separator + channelName + File.separator + date;
            logger.info("开始从微信渠道下载前一天的对账文件");
            for (PartnerEnum partnerEnum : PartnerEnum.values()) {
                String parterId = partnerEnum.getPartnerId();
                String appId = partnerEnum.getAppId();
                Map<String, String> requestParam = buildBillInfParam(parterId, appId);
                String xmlData = WechatPayUtil.map2Xml(requestParam);
                String responseStr = HttpClientUtils.executePostReq(billUrl, xmlData);
                String fileName = channelName + "-fi-" + parterId + "-" + date;//微信只有收款
                File fileDir = new File(filePath);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(responseStr.getBytes()));
                String path = filePath + File.separator + fileName + ".txt";
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
                int len = 2048;
                byte[] b = new byte[len];
                while ((len = bis.read(b)) != -1) {
                    bos.write(b, 0, len);
                }
                bos.flush();
                try {
                    bis.close();
                    bos.close();
                } catch (IOException ioe) {

                }
                logger.info("商户号{}文件对账单下载成功，{}", parterId, path);
                //对文件进行zip压缩存放
                String zipFilePath = zipFile(path);
                if (zipFilePath == null) {
                    logger.error("文件压缩失败！");
                }
            }
            //文件上传至sftp服务器
            uploadFiles(filePath, channelName);

        } catch (Exception e) {
            logger.error("下载文件对账单失败！");
        }
    }

    private Map<String, String> buildBillInfParam(String parterId, String appId) {
        Map<String, String> paremMap = new HashMap<>();
        paremMap.put("appid", appId);
        paremMap.put("mch_id", parterId);
        paremMap.put("nonce_str", WechatPayUtil.genNonceString());
        //获取对账日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date dBefore = calendar.getTime();
        paremMap.put("bill_date", new SimpleDateFormat("yyyyMMdd").format(dBefore));
        paremMap.put("bill_type", "SUCCESS"); //成功支付的订单
        paremMap.put("sign", WechatPayUtil.signMD5(paremMap, WECHAT_APP_KEY)); //所有订单类型，默认值
        return paremMap;
    }

    public enum PartnerEnum {

        PARTNER1("1495297853", "wg5fbhbb3j5bf4479e");

        PartnerEnum(String parterId, String appId) {
            this.partnerId = parterId;
            this.appId = appId;
        }

        private String partnerId;
        private String appId;

        public String getPartnerId() {
            return partnerId;
        }

        public String getAppId() {
            return appId;
        }

    }

}

