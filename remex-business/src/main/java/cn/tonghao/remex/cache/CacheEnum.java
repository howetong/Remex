package cn.tonghao.remex.cache;

public enum CacheEnum {

    /**
     * 全局字典配置
     */
    CONFIG_VALUE("REMEX_CONFIG_VALUE_", 3600);

    // key 的前缀
    private String keyPre;
    // 有效期
    private int expire;

    /**
     * 创建一个新的实例 CacheEnum.
     *
     * @param keyPre
     * @param expire
     */
    private CacheEnum(String keyPre, int expire) {
        this.keyPre = keyPre;
        this.expire = expire;
    }

    /**
     * keyPre
     *
     * @return the keyPre
     */

    public String getKeyPre() {
        return keyPre;
    }

    /**
     * @param keyPre the keyPre to set
     */

    public void setKeyPre(String keyPre) {
        this.keyPre = keyPre;
    }

    /**
     * expire
     *
     * @return the expire
     */

    public int getExpire() {
        return expire;
    }

    /**
     * @param expire the expire to set
     */

    public void setExpire(int expire) {
        this.expire = expire;
    }

}
