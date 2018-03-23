package cn.tonghao.remex.business.bill;

import cn.tonghao.remex.business.core.log.RemexLogger;
import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Properties;

public class SFTPChannel {

    private static Logger logger = RemexLogger.getLogger(SFTPChannel.class);

    public static final int SFTP_DEFAULT_PORT = 22;

    Session session;

    Channel channel;

    public ChannelSftp getChannel(Map<String, String> sftpDetails, int timeout) throws JSchException {

        String ftpHost = sftpDetails.get("host");
        String port = sftpDetails.get("port");
        String ftpUserName = sftpDetails.get("username");
        String ftpPassword = sftpDetails.get("password");

        int ftpPort = SFTP_DEFAULT_PORT;
        if (StringUtils.isNoneBlank(port)) {
            ftpPort = Integer.valueOf(port);
        }

        JSch jsch = new JSch(); // 创建JSch对象
        session = jsch.getSession(ftpUserName, ftpHost, ftpPort); // 根据用户名，主机ip，端口获取一个Session对象
        if (ftpPassword != null) {
            session.setPassword(ftpPassword); // 设置密码
        }
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(timeout); // 设置timeout时间
        session.connect(); // 通过Session建立链接

        // 打开SFTP通道
        channel = session.openChannel("sftp");
        channel.connect(); // 建立SFTP通道的连接
        logger.info("Connected successfully to ftpHost = " + ftpHost + ",as ftpUserName = " + ftpUserName + ", returning: " + channel);
        return (ChannelSftp) channel;
    }

    public void closeChannel() {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }

}
