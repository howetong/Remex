package cn.tonghao.remex.common.util.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * Http请求工具类
 * Created by tonghao on 2017/5/8.
 */
public class HttpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private static RequestConfig requestConfig = null;

    private static PoolingHttpClientConnectionManager connMgr = null;

    static{
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
        //设置连接超时时间
        requestConfig = RequestConfig.custom()
                .setConnectTimeout(2000) //设置连接超时时间
                .setConnectionRequestTimeout(2000) //设置从连接池获取连接实例超时时间
                .setSocketTimeout(5000).build(); //设置读取超时时间
    }

    private HttpClientUtils() {
    }

    public static String doPost(String url, String str) {

        //获取默认实例
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        //设置参数到请求对象中
        StringEntity entity = new StringEntity(str,"utf-8");
        httpPost.setEntity(entity);
        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader("Content-type", "application/json");
        String responseStr = null;
        CloseableHttpResponse response = null;
        try {
            //执行请求操作，并拿到结果（同步阻塞）
            response = httpClient.execute(httpPost);
            HttpEntity respEntity = response.getEntity();
            if (respEntity != null){
                responseStr = EntityUtils.toString(respEntity,"utf-8");
            }
            EntityUtils.consume(entity);
        } catch (IOException e) {
            //调用异常，timeOut或者协议错误
            logger.info("http请求异常！,{}", e);
        } finally {
            try {
                if(response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                logger.error("资源释放异常, {}", e);
            }
        }
        return responseStr;
    }



    /**
     * 信任自定义证书
     * @param url 请求url
     * @param param 请求参数
     * @return 返回请求结果字符串
     */
    public static String executePostReqWithCert(String url, String param,String keyStore,String keyPass) {

        SSLContext sslContext = createCertSSL(keyStore,keyPass);
        //设置连接工厂
        SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslContext);
        //创建httpClient对象
        CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslFactory).setConnectionManager(connMgr).build();

        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        //设置连接超时时间
        httpPost.setConfig(requestConfig);
        //设置参数到请求对象中
        StringEntity entity = new StringEntity(param,"utf-8");
        httpPost.setEntity(entity);
        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader("Content-type", "application/json");
        return executeHttpPost(httpPost,client,entity);
    }

    /**
     * 信任所有证书,绕过验证
     * @param url 请求url
     * @param param 请求参数
     * @return String　返回请求结果
     */
    public static String executePostReq(String url, String param){
        try{
            //信任所有证书
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> {
                return true;
            }).build();
            //设置连接工厂
            SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslContext,new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);

            //创建httpClient对象
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslFactory).setConnectionManager(connMgr).build();

            //创建post方式请求对象
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            //设置参数到请求对象中
            StringEntity entity = new StringEntity(param,"utf-8");
            httpPost.setEntity(entity);
            //设置header信息
            //指定报文头【Content-type】、【User-Agent】
            httpPost.setHeader("Content-type", "application/json");
            return executeHttpPost(httpPost, httpClient, entity);
        } catch(NoSuchAlgorithmException | KeyStoreException | KeyManagementException e){
            logger.info("https请求出现异常,{}",e.getMessage());
            return null;
        }
    }

    /**
     * 执行post请求并返回影响结果字符串
     * @param httpPost post请求
     * @param httpClient httpclient客户端
     * @return 响应字符串
     */
    private static String executeHttpPost(HttpPost httpPost, CloseableHttpClient httpClient, StringEntity entity){
        String responseStr = null;
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity respEntity = response.getEntity();
            if (respEntity != null){
                responseStr = EntityUtils.toString(respEntity,"utf-8");
            }
            EntityUtils.consume(entity);
        } catch (IOException e) {
            //调用异常，timeOut或者协议错误
            logger.info("http请求异常！,{}", e);
        }
        return responseStr;
    }

    /**
     * 返回通过证书验证的SSLContext
     * @param keyStorePath 密钥库路径
     * @param keyStorePass 密钥库密码
     * @return SSLContext
     */
    private static SSLContext createCertSSL(String keyStorePath, String keyStorePass){
        SSLContext sc = null;
        FileInputStream instream = null;
        KeyStore trustStore;
        try{
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            instream = new FileInputStream(new File(keyStorePath));
            trustStore.load(instream, keyStorePass.toCharArray());
            // 相信自己的CA和所有自签名的证书
            sc = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
            //如果要验证客户端证书，则要加载客户端证书
            //loadKeyMaterial(trustStore, certPwd.toCharArray()).build
        }catch (KeyStoreException | NoSuchAlgorithmException| CertificateException | IOException | KeyManagementException e) {
            logger.info("生成SSL上下文异常, {}", e);
        } finally {
            try {
                if (instream != null)
                    instream.close();
            } catch (IOException e) {
                logger.info("资源关闭异常",e.getMessage());
            }
        }
        return sc;
    }

}

