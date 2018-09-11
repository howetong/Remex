package cn.tonghao.remex.business.core.util;

import cn.tonghao.remex.business.core.log.RemexLogger;
import cn.tonghao.remex.common.util.CompressUtil;
import net.lingala.zip4j.exception.ZipException;
import org.slf4j.Logger;

import java.io.*;
import java.util.regex.Pattern;

/**
 * 文件解析工具
 * Created by howetong on 2018/3/23.
 */
public class FileResolveUtils {

    private static Logger logger = RemexLogger.getLogger(FileResolveUtils.class);

    //将path路径下的zip格式账单转换为能通过excel打开的csv格式
    public static void dealWithBill(String path) {
        try{
            File[] files = CompressUtil.unzip(path, null);
            for (File file : files) {
                String outPutFile = path.substring(0, path.indexOf(".zip")) + ".csv";
                readCSVFile(file, outPutFile);
            }
        } catch (ZipException ex) {
            logger.error("[dealWithBill]宝付zip对账文件解析失败！");
        }
    }

    private static void readCSVFile(File file, String outPutFile){
        try {
            String encoding="utf-8";
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPutFile),"GBK"));
                String lineTxt, temp, lineTxtNew;
                Pattern pattern = Pattern.compile("^\\d{16,}$");
                StringBuilder sb = new StringBuilder();
                while((lineTxt = bufferedReader.readLine()) != null){
                    temp = lineTxt.replace("|",",");
                    String items[] = temp.split(",");
                    for (String item : items) {
                        if (pattern.matcher(item).find()) {
                            item += "\t";
                        }
                        sb = sb.append(item + ",");
                    }
                    lineTxtNew = sb.toString();
                    bufferedWriter.write(lineTxtNew);
                    bufferedWriter.write("\r\n");
                    sb.delete(0,sb.length());
                }
                bufferedWriter.flush();
                read.close();
            }else{
                logger.error("找不到指定的文件");
            }
        } catch (Exception e) {
            logger.error("读取文件内容出错,e", e);
        }
    }
}
