package cn.tonghao.remex.dao.impl;

import cn.tonghao.remex.dao.IPropertyConfigDao;
import cn.tonghao.remex.domain.PropertyConfig;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by howetong on 2017/2/28.
 */
@Repository
public class PropertyConfigDaoImlp implements IPropertyConfigDao {

    @Resource(name = "sqlSessionTemplate")
    private SqlSessionTemplate sst;

    private final String namespace = PropertyConfigDaoImlp.class.getName().concat(".");

    @Override
    public PropertyConfig findByPropertyTypeAndKey(String propertyType, String propertyKey) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("propertyType", propertyType);
        map.put("propertyKey", propertyKey);
        return sst.selectOne(namespace + "findByPropertyTypeAndKey", map);
    }
}
