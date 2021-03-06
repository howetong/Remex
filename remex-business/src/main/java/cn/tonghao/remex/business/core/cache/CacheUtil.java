package cn.tonghao.remex.business.core.cache;

import cn.tonghao.remex.business.core.log.RemexLogger;
import cn.tonghao.remex.common.util.DateTimeUtil;
import cn.tonghao.remex.common.util.JsonUtil;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

@Repository
public class CacheUtil {

    private static final Logger LOGGER = RemexLogger.getLogger(CacheUtil.class);

    @Resource
    private ShardedJedisPool jedisPool;

    //yyyy-MM-dd HH:mm:ss
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DateTimeUtil.TIME_FORMAT);

    public boolean lock(String key) {
        return lock(key, 10);
    }

    public boolean lock(String key, int expireTime) {
        boolean excp = false;
        try {
            long lock = incr(key);
            LOGGER.info("key {} lock times {}", key, lock);
            return lock > 0 && lock < 2;
        } catch (Exception e) {
            excp = true;
            return true;
        } finally {
            if (!excp) {
                expire(key, expireTime);
            }
        }
    }

    /**
     * incr操作
     *
     * @param key 键名
     * @return long
     */
    public long incr(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.incr(key);
        } catch (JedisConnectionException e) {
            LOGGER.error("incr key {} error...", key, e);
            broken(jedis);
            throw e;
        } finally {
            release(jedis);
        }
    }

    public void expire(String key, int seconds) {
        ShardedJedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.expire(key, seconds);
        } catch (JedisConnectionException e) {
            LOGGER.error("expire key {} in {} secs error...", key, seconds, e);
            broken(jedis);
        } finally {
            release(jedis);
        }
    }



    /**
     * @param key 缓存的key
     * @param cls 欲转换的类
     * @return T
     */
    public <T> T getCache(String key, Class<T> cls) {
        return JsonUtil.toBean(this.getCache(key), cls);
    }

    /**
     * @param key 缓存的key
     * @return String
     */
    public String getCache(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        } catch (JedisConnectionException e) {
            LOGGER.error("get cache {} error...", key, e);
            broken(jedis);
            return null;
        } finally {
            release(jedis);
        }
    }

    /**
     * 此方法描述的是：设置缓存
     *
     * @param key    键
     * @param value  值
     * @param expire 有效时间
     */
    public void setCache(String key, Object value, int expire) {
        ShardedJedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.setex(key, expire, JsonUtil.toString(value, dateFormat));
        } catch (JedisConnectionException e) {
            LOGGER.error("set cache ({},{},{}) error...", new Object[]{key, value, expire}, e);
            broken(jedis);
        } finally {
            release(jedis);
        }
    }

    /**
     * 此方法描述的是：缓存删除
     *
     * @param key
     */
    public void delCache(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
        } catch (JedisConnectionException e) {
            LOGGER.error("del cache {} error...", key, e);
            broken(jedis);
        } finally {
            release(jedis);
        }
    }

    /**
     * 此方法描述的是：使用SETNX命令设置缓存
     * SETNX 即是 : set if not exists
     * set成功返回1、set失败返回0
     *
     * @param key
     */
    public Long setNxCache(String key, String value) {
        ShardedJedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.setnx(key, value);
        } catch (JedisConnectionException e) {
            LOGGER.error("del cache {} error...", key, e);
            broken(jedis);
        } finally {
            release(jedis);
        }
        return 0L;
    }

    public void setExpire(String key, int expire) {
        ShardedJedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.expire(key, expire);
        } catch (JedisConnectionException e) {
            LOGGER.error("del cache {} error...", key, e);
            broken(jedis);
        } finally {
            release(jedis);
        }
    }

    /**
     * 连接释放
     *
     * @param jedis
     */
    private void release(ShardedJedis jedis) {
        if (jedis != null) {
            try {
                jedisPool.returnResource(jedis);
            } catch (JedisException e) {
                LOGGER.error("could not release resource...", e);
            }
        }
    }

    /**
     * 连接销毁
     *
     * @param jedis
     */
    private void broken(ShardedJedis jedis) {
        if (jedis != null) {
            try {
                jedisPool.returnBrokenResource(jedis);
            } catch (JedisException e) {
                LOGGER.error("could not release broken resource...", e);
            }
        }
    }
}
