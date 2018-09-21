package cn.tonghao.remex.business.bill.impl;

import cn.tonghao.remex.business.bill.CommonFileBillStrategy;
import cn.tonghao.remex.business.bill.IChannelFileBillStrategy;
import cn.tonghao.remex.business.core.log.RemexLogger;
import cn.tonghao.remex.business.core.util.FileResolveUtils;
import cn.tonghao.remex.common.util.DateTimeUtil;
import cn.tonghao.remex.common.util.http.HttpsURLConnUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * baofoo 对账单获取策略
 * Created by howetong on 2018/9/11.
 */
@Service("baofooFileBillStrategy")
public class BaofooFileBileStrategyImpl extends CommonFileBillStrategy implements IChannelFileBillStrategy {

    private static final Logger logger = RemexLogger.getLogger(BaofooFileBileStrategyImpl.class);

    public static final String VERSION = "4.0.0.2";

    private static final String CLINET_IP = "211.148.24.228";

    private static final String channelName = "baofoo"; //渠道名作为文件子目录

    private static final String API_URL = "https://vgw.baofoo.com/boas/api/fileLoadNewRequest";

    @Override
    public void getFillBill() {
        try {
            //当前日期前一天
            String date = DateTimeUtil.getDateBeforeDay("yyyyMMdd");
            //本地文件保存路径
            String filePath = System.getProperty("catalina.base") + File.separator + channelName + File.separator + date;
            //对账文件下载到本地
            logger.info("开始从宝付渠道下载前一天的对账文件");
            for (PartnerIdEnum partnerIdEnum : PartnerIdEnum.values()) {
                String partnerId = partnerIdEnum.getId();
                Map<String, String> requestParam = buildBillInfParam(partnerId, partnerIdEnum.getType());
                String responseStr = HttpsURLConnUtils.executePostUTF(requestParam, API_URL);
                if (responseStr == null || !responseStr.contains("resp_code=0000")) {
                    logger.error("下载失败!宝付商户号：{}, 原因：{}", partnerId, responseStr == null ? "渠道返回为null" : responseStr.substring(responseStr.indexOf("resp_msg")));
                    continue;
                }
                String[] splitStr = responseStr.split("=");    //解板返回的文件参数
                //进行base64解码
                byte[] fileBytes = new BASE64Decoder().decodeBuffer(splitStr[3]);

                String fileName = channelName + "-" + partnerIdEnum.getType() + "-" + partnerId + "-" + date;
                File fileDir = new File(filePath);
                if (!fileDir.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    fileDir.mkdirs();
                }
                BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(fileBytes));
                String path = filePath + File.separator + fileName + ".zip";
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
                    logger.warn("io流关闭异常");
                }
                logger.info("商户号{}文件对账单下载成功", partnerId);
                // 文件解析
                FileResolveUtils.dealWithBill(path);
            }
            //文件上传至sftp服务器
            uploadFiles(filePath, channelName);
        } catch (Exception e) {
            logger.error("下载文件对账单失败！原因是：{}", e.getMessage());
        }
    }


    private Map<String, String> buildBillInfParam(String partnerId, String type) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("version", VERSION);
        paramMap.put("member_id", partnerId);
        paramMap.put("file_type", type); //fi 收款  fo 放款
        paramMap.put("client_ip", CLINET_IP);
        paramMap.put("settle_date", DateTimeUtil.getDateBeforeDay("yyyy-MM-dd"));
        return paramMap;
    }


    public enum PartnerIdEnum {
        //以下收款
        PARTNERID0("100000276", "fi"),
        //fi表示收款，fo表示放款
        ;

        PartnerIdEnum(String id, String type) {
            this.id = id;
            this.type = type;
        }

        private String id;

        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
