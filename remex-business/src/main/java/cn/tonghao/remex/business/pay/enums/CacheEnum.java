package cn.tonghao.remex.business.pay.enums;

/**
 * Created by howetong on 2018/5/14.
 */
public enum CacheEnum {
    /**
     * 字典缓存
     */
    CASHIER_DICTIONARY_CODE("REMEX_PAY_CODE_DICTIONARY", 1800),
    ;

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

    public String getKey(String... suffix) {
        if (suffix != null && suffix.length > 0) {
            StringBuilder sb = new StringBuilder(this.keyPre);
            for (String fix : suffix) {
                sb.append("_").append(fix);
            }
            return sb.toString();
        }
        return null;
    }

}