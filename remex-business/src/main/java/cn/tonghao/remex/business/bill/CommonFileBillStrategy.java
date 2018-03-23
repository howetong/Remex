package cn.tonghao.remex.business.bill;

import cn.tonghao.remex.business.core.log.RemexLogger;
import cn.tonghao.remex.common.util.CompressUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 对账单处理通用类，可以被具体渠道对账单处理类继承
 * Created by howetong on 2018/2/12.
 */
@Service
public class CommonFileBillStrategy {

    private static final Logger logger = RemexLogger.getLogger(CommonFileBillStrategy.class);

    @Value("${channel.bill.sftp.req.host}")
    private String host;
    @Value("${channel.bill.sftp.req.port}")
    private String port;
    @Value("${channel.bill.sftp.req.username}")
    private String username;
    @Value("${channel.bill.sftp.req.password}")
    private String password;
    @Value("${channel.bill.sftp.req.absolutePath}")
    private String absolutePath;

    protected void uploadFiles(String filePath, String channelName) {
        //获取当前目录下的所有文件
        List<String> nowLocalDirFiles = getNowLocalDirFiles(filePath);
        for (String fileName : nowLocalDirFiles) {
            doUploadToSFTP(filePath, fileName, channelName);
        }
        //删除本地临时目录文件
        doDeleteLocalTmpDir(nowLocalDirFiles, filePath);
    }


    //获取当前路径下所有文件，返回文件名list
    private List<String> getNowLocalDirFiles(String filePath) {
        List<String> fileList = new ArrayList<String>();
        File dir = new File(filePath);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                String fileName = file.getName();
                fileList.add(fileName);
            }
        }
        return fileList;
    }

    private void doUploadToSFTP(String filePath, String fileName, String channelName) {
        //待上传的完整文件名
        String origFileName = filePath + File.separator + fileName;
        Map<String, String> sftpDetails = new HashMap<String, String>();
        // 设置主机ip，端口，用户名，密码
        sftpDetails.put(Constants.SFTP_REQ_HOST, host);
        sftpDetails.put(Constants.SFTP_REQ_PORT, port);
        sftpDetails.put(Constants.SFTP_REQ_USERNAME, username);
        sftpDetails.put(Constants.SFTP_REQ_PASSWORD, password);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date dBefore = calendar.getTime();
        String parentDir = new SimpleDateFormat("yyyyMMdd").format(dBefore);
        String channelDir = absolutePath + File.separator + channelName;
        String destFilePath = channelDir + File.separator  + parentDir;
        //获取sftp通道
        ChannelSftp channel = null;
        SFTPChannel sftpChannel = new SFTPChannel();
        try {
            channel = sftpChannel.getChannel(sftpDetails, 60000);
//            //解决上传文件中中文名乱码问题  无法解决！
//            Field field = ChannelSftp.class.getDeclaredField("server_version");
//            field.setAccessible(true);
//            field.set(channel, 2);
//            channel.setFilenameEncoding("GBK");
            //创建目标文件夹
            channel.cd(absolutePath);
            Vector dirs = channel.ls(absolutePath);
            boolean isExistChnnelDir = false;
            boolean isExistFinnalDir = false;
            for(int i = 0; i < dirs.size(); i++){
                if(((ChannelSftp.LsEntry)dirs.get(i)).getFilename().equals(channelName)){
                    isExistChnnelDir = true; //存在渠道文件夹
                    channel.cd(channelDir);
                    Vector files = channel.ls(channelDir);
                    for (int j = 0; j < files.size(); j++) {
                        if (((ChannelSftp.LsEntry)files.get(j)).getFilename().equals(parentDir)) {
                            isExistFinnalDir = true;
                            break;
                        }
                    }
                }
            }
            //创建ftp目录(jsch不能创建多级目录)
            if(!isExistChnnelDir){
                channel.mkdir(channelName);
                channel.cd(channelDir);
                channel.mkdir(parentDir);
            } else if (!isExistFinnalDir) {
                channel.mkdir(parentDir);
            }
            channel.put(origFileName, destFilePath, ChannelSftp.OVERWRITE);
        } catch (JSchException e) {
            logger.info("ftp服务器连接异常，原因是:{}",e.getMessage());
        } catch (Exception e) {
            logger.info("文件上传异常，原因是:{}", e.getMessage());
        } finally {
            if (channel != null) {
                channel.quit();
            }
            sftpChannel.closeChannel();
        }
    }

    /**
     * 删除本地临时目录
     */
    private void doDeleteLocalTmpDir(List<String> fileLists, String filePath) {
        if(CollectionUtils.isNotEmpty(fileLists)) {
            for(String fileName : fileLists) {
                File file = new File(filePath + File.separator + fileName);
                if(file.exists()){
                    file.delete();
                }
            }
        }
        File dir = new File(filePath);
        if(dir.listFiles() != null && dir.listFiles().length == 0){
            dir.delete();
        }
    }

    protected String zipFile(String path){
        String zipFilePath = CompressUtil.zip(path);
        if (zipFilePath != null) {
            new File(path).delete();
        }
        return zipFilePath;
    }

    /**
     * 删除指定day以前的账单文件
     */
    public void doDeleteFiles(int day) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString= formatter.format(currentTime);
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        SFTPChannel sftpChannel = new SFTPChannel();
        Map<String, String> sftpDetails = new HashMap<String, String>();
        // 设置主机ip，端口，用户名，密码
        sftpDetails.put(Constants.SFTP_REQ_HOST, host);
        sftpDetails.put(Constants.SFTP_REQ_USERNAME, username);
        sftpDetails.put(Constants.SFTP_REQ_PASSWORD, password);
        sftpDetails.put(Constants.SFTP_REQ_PORT, port);
        ChannelSftp channel = null;
        try {
            channel = sftpChannel.getChannel(sftpDetails, 60000);
            doDeleteSftpFile(channel, absolutePath, dateString, df, day);
        } catch (JSchException e) {
            logger.info("ftp服务器连接异常，原因是:{}",e.getMessage());
            return;
        } finally {
            if (channel != null) {
                channel.quit();
            }
            sftpChannel.closeChannel();
        }
    }

    private void doDeleteSftpFile(ChannelSftp channel, String path, String dateString, DateFormat df, int day) {
        try {
            Vector lsts= channel.ls(path);
            channel.cd(path);
            for(int i = 0; i < lsts.size(); i++){
                //当前日期
                Date d1 = df.parse(dateString);
                String channelName = ((ChannelSftp.LsEntry)lsts.get(i)).getFilename();
                //过滤掉.和..
                if (channelName.equals(".") || channelName.equals("..")) {
                    continue;
                }
                String channelDir = path + File.separator + channelName;
                channel.cd(channelDir);
                Vector dirs = channel.ls(channelDir);
                for (int j = 0; j < dirs.size(); j++){
                    String date = ((ChannelSftp.LsEntry)dirs.get(j)).getFilename();
                    if(date.length() == 8){
                        Date d2 = df.parse(date);
                        long diff = d1.getTime() - d2.getTime();
                        long days = diff / (1000*60*60*24);
                        if(days > day){
                            Vector dateVs= channel.ls(date);
                            for(int k = 0; k < dateVs.size(); k++){
                                String fileName =((ChannelSftp.LsEntry)dateVs.get(k)).getFilename();
                                if (fileName.equals(".") || fileName.equals("..")) {
                                    continue;
                                }
                                channel.rm(date + File.separator + fileName);
                            }
                            channel.rmdir(date);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.info("文件删除失败，原因：{}", e.getMessage());
        }
    }
}
