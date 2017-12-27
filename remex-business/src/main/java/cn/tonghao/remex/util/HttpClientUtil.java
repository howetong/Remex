package cn.tonghao.remex.util;

import cn.tonghao.remex.log.RemexLogger;
import org.apache.http.HttpEntity;
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
public class HttpClientUtil {

    private static RemexLogger logger = (RemexLogger) LoggerFactory.getLogger(RemexLogger.class);

    private static RequestConfig requestConfig = null;

    static{
        //设置连接超时时间
        requestConfig = RequestConfig.custom().setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000).setSocketTimeout(20000).build();
    }

    private HttpClientUtil() {
    }

    public static String sendData(String url, String str) {
        SSLConnectionSocketFactory sslFactory = SSLConnectionSocketFactory.getSocketFactory();

        //创建httpClient对象
        CloseableHttpClient client = getHttpClient(sslFactory);

        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        //设置参数到请求对象中
        StringEntity entity = new StringEntity(str,"utf-8");
        httpPost.setEntity(entity);
        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        httpPost.setHeader("Cookie","JSESSIONID=423373580E35C43CFBD4EF364792C5A8");
        return executeHttpPost(httpPost,client);
    }



    /**
     * 信任自定义证书
     * @param url 请求url
     * @param param 请求参数
     * @return 返回请求结果字符串
     */
    public static String sendDataWithCert(String url, String param,String keyStore,String keyPass) {

        SSLContext sslContext = createCertSSL(keyStore,keyPass);
        //设置连接工厂
        SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslContext);
        //创建httpClient对象
        CloseableHttpClient client = getHttpClient(sslFactory);

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
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        return executeHttpPost(httpPost,client);
    }

    /**
     * 信任所有证书,绕过验证
     * @param url 请求url
     * @param param 请求参数
     * @return String　返回请求结果
     */
    public static String sendDataWithAllCerts(String url, String param){
        try{
            //信任所有证书
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> {
                return true;
            }).build();
            //设置连接工厂
            SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslContext,new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);

            //创建httpClient对象
            CloseableHttpClient client = getHttpClient(sslFactory);

            //创建post方式请求对象
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            //设置参数到请求对象中
            StringEntity entity = new StringEntity(param,"utf-8");
            httpPost.setEntity(entity);
            //设置header信息
            //指定报文头【Content-type】、【User-Agent】
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            return executeHttpPost(httpPost,client);
        }catch(NoSuchAlgorithmException | KeyStoreException | KeyManagementException e){
            logger.info("https请求出现异常,{}",e.getMessage());
            return null;
        }
    }


    private static CloseableHttpClient getHttpClient(SSLConnectionSocketFactory sslFactory){

        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslFactory)
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 将最大连接数增加到200
        connManager.setMaxTotal(200);
        // 将每个路由基础的连接增加到30
        connManager.setDefaultMaxPerRoute(30);
        //创建httpClient对象
        return HttpClients.custom().setConnectionManager(connManager).build();
    }

    /**
     * 执行post请求并返回影响结果字符串
     * @param httpPost post请求
     * @param client httpclient客户端
     * @return 响应字符串
     */
    private static String executeHttpPost(HttpPost httpPost,CloseableHttpClient client){
        CloseableHttpResponse response;
        try {
            //执行请求操作，并拿到结果（同步阻塞）
            response = client.execute(httpPost);
            HttpEntity respEntity = response.getEntity();
            if (respEntity != null){
                return EntityUtils.toString(respEntity);
            }
        } catch (IOException e) {
            //调用异常，timeOut或者协议错误
            logger.info("http请求异常！,{}",e.getMessage());
        }
        return null;
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
        }catch (KeyStoreException | NoSuchAlgorithmException| CertificateException | IOException | KeyManagementException e) {
            logger.info("生成SSL上下文异常",e.getMessage());
            e.printStackTrace();
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

