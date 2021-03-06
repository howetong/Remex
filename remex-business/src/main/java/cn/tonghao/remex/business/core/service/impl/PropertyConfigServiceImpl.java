package cn.tonghao.remex.business.core.service.impl;

import cn.tonghao.remex.business.core.cache.CacheUtil;
import cn.tonghao.remex.business.core.dao.IPropertyConfigDao;
import cn.tonghao.remex.business.core.domain.PropertyConfig;
import cn.tonghao.remex.business.core.service.IPropertyConfigService;
import cn.tonghao.remex.business.core.cache.CacheEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PropertyConfigServiceImpl implements IPropertyConfigService {

    private static final String PROPERTY_TYPE = "fbs-payc";

    @Resource
    private CacheUtil cacheUtil;

    @Resource
    private IPropertyConfigDao propertyConfigDao;

    @Override
    public boolean setValue(String key, String value) {
        return false;
    }

    @Override
    public String getValue(String key) {
        if (null == key || key.isEmpty()) {
            return null;
        }

        String redisKey = CacheEnum.CONFIG_VALUE.getKeyPre() + key;
        String cache = cacheUtil.getCache(redisKey);
        if (null != cache) {
            return cache;
        }

        PropertyConfig propertyConfig = propertyConfigDao.findByPropertyTypeAndKey(PROPERTY_TYPE, key);

        // 不为空，则缓存
        if (null != propertyConfig && null != propertyConfig.getPropertyValue()) {
            cacheUtil.setCache(redisKey, propertyConfig.getPropertyValue(), CacheEnum.CONFIG_VALUE.getExpire());
            return propertyConfig.getPropertyValue();
        }

        return null;
    }
}
