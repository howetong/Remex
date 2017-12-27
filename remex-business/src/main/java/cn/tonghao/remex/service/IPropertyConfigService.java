package cn.tonghao.remex.service;

/**
 * 配置服务，内置redis缓存
 */
public interface IPropertyConfigService {

    /**
     * 设置配置键值
     *
     * @param key   键
     * @param value 值
     * @return 设置成功，返回true，否则，false
     */
    boolean setValue(final String key, final String value);

    /**
     * 根据键获取配置值
     *
     * @param key 键
     * @return 配置值
     */
    String getValue(final String key);
}
