package cn.tonghao.remex.business.core.properties.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

/**
 * 读取数据库配置信息放到properties中
 * Created by tonghao on 2017/8/10.
 */
public class ExtPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    private static final Logger log = LoggerFactory.getLogger(ExtPropertyPlaceholderConfigurer.class);

    private List<String> properties;
    private DataSource ds;

    /**
     * 多查询一个带后缀的类型，便于相同应用使用不同配置
     */
    private String sql = "select property_key,property_value from property_config_data where property_type = ? or property_type=? order by property_type";

    /**
     * 重载父类
     */
    @Override
    protected void processProperties(ConfigurableListableBeanFactory factory, Properties props) throws BeansException {
        Properties locationProps = null;
        try {
            //获取父类配置文件
            locationProps = super.mergeProperties();
        } catch (IOException e) {
            logger.error(" location_props Error ",e);
        }
        if (locationProps == null) {
            locationProps = new Properties();
        }
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String suffixType=getSuffixType();

            for (String type : properties) {
                ps.setString(1,type);
                type=type+suffixType;
                ps.setString(2, type);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String key = rs.getString("property_key");
                        if(key.startsWith(" ")||key.endsWith(" ")){
                            log.warn("***************** key contain space,key=[{}]", key);
                            key=key.trim();
                        }

                        if(StringUtils.isBlank(key))
                        {
                            continue;
                        }
                        String value = rs.getString("property_value");
                        if(null != value)
                        {
                            if(key.startsWith("system.")){
                                System.setProperty(key.substring(7), value);
                            } else {
                                locationProps.put(key,value);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("SQL Error",e);
        }
        super.processProperties(factory, locationProps);
    }


    /**
     * 优先使用启动参数配置的containerId作为后缀
     * 在没有配置containerId的情况使用ip作为后缀
     */
    private String getSuffixType(){
        String suffixType=null;
        String containerId = System.getProperty("containerId");
        if(StringUtils.isNotBlank(containerId)){
            suffixType = containerId;
        }
        return suffixType;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    public DataSource getDs() {
        return ds;
    }

    public void setDs(DataSource ds) {
        this.ds = ds;
    }
}
