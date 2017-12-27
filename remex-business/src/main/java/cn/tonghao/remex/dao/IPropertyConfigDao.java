package cn.tonghao.remex.dao;

import cn.tonghao.remex.domain.PropertyConfig;

/**
 * Created by howetong on 2017/12/27.
 */
public interface IPropertyConfigDao {

    PropertyConfig findByPropertyTypeAndKey(String propertyType, String propertyKey);
}
