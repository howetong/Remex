package cn.tonghao.remex.common.util;

import cn.tonghao.remex.common.exception.BusinessException;
import cn.tonghao.remex.common.exception.ErrorCodeDefinition;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 下载工具类
 */
public class FileDownloadUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileDownloadUtil.class);
	
	/**
	 * 根据文件url路径下载文件
	 */
	public static void downloadFile(String url, HttpServletRequest request, HttpServletResponse response) {
		if(StringUtils.isNotBlank(url)){
			InputStream in = null;
			try{
		        Pattern pattern = Pattern.compile("(\\.(\\w+)\\?)|(\\.(\\w+)$)");
		        Matcher matcher = pattern.matcher(url);
		        String fileType = null;
		        while (matcher.find()) {
		            fileType = matcher.group();
		            break;
		        }
		        //获取文件类型
		        if (fileType != null) {
		            fileType = fileType.replaceAll("\\?", "");
		        }
		
		        //读取文件
		        URL urlObject = new URL(url);
		        in = urlObject.openStream();
		        byte[] data = IOUtils.toByteArray(in);
		        
		        String fileName = url.contains("/") ? url.substring(url.lastIndexOf("/")+1) : url;//文件名与url文件名一致	//System.currentTimeMillis() + "_file";
		        response.setContentType("charset=UTF-8");//字符集
		        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName + fileType, "UTF-8"));//头信息
		
		        //输出流到页面
		        response.getOutputStream().write(data);
		        response.getOutputStream().flush();
		        
		        response.getOutputStream().write("window.close();<script>".getBytes());
			}catch(Exception e){
				logger.error("导出文件异常:{}", e);
				throw new BusinessException(ErrorCodeDefinition.ERROR_DOWNLOAD_FILE_FAIL);
			} finally {
				if(null != in){
					try {
						in.close();
					} catch (IOException e) {
						logger.error("导出文件异常:{}", e);
					}
				}
			}
		
		}
	}
}
