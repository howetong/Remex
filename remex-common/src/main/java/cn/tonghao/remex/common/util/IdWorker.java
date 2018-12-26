package cn.tonghao.remex.common.util;

/**
 * 基于witter Snowflake 雪花算法的分布式唯一id解决方案
 * 一个long型变量共64位，转换成十进制共19位。从左到右划分为：
 * 第1位：0，暂时没有利用
 * 第2位到44位：当前毫秒
 * 第45位到第49位：数据中心id
 * 第50位到第54位：机器id
 * 第55位到第64位：一个毫秒内的自增id
 *
 * * 不同节点需要指定机器id(workerId)和数据中心id(dataCenterId)
 * @author howetong
 * @Date 2018/12/6.
 */
public class IdWorker {

    // start point: 2018-10-01 00:00:00
    private final long startTimestamp = 1538323200000L;
    // 机器标识位数
    private final long workerIdBits = 5L;
    // 数据中心标识位数
    private final long dataCenterIdBits = 5L;
    // 机器ID最大值
    private final long maxWorkerId = ~(-1L << workerIdBits);
    // 数据中心ID最大值
    private final long maxDataCenterId = ~(-1L << dataCenterIdBits);
    // 一个毫秒内自增位数
    private final long milliSecondBits = 12L;
    // 机器ID左移位数：12位
    private final long workerIdShift = milliSecondBits;
    // 数据中心ID左移位数：17位(=12+5）
    private final long dataCenterIdShift = milliSecondBits + workerIdBits;
    // 时间毫秒左移位数：22位(=12+5+5）
    private final long timestampLeftShift = milliSecondBits + workerIdBits + dataCenterIdBits;
    private final long maxAutoIncrementNum = ~(-1L << milliSecondBits);

    private long workerId;
    private long dataCenterId;
    private long autoIncrementNum = 0L;
    private long lastTimestamp = -1L;

    /**
     * constructor
     *
     * @param workerId vm/pc id
     * @param dataCenterId data center id
     * @return
     */
    public IdWorker(long workerId, long dataCenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker d can't be greater than %d or less than 0", maxWorkerId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("Data center Id can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * get next available id
     *
     * @return long
     */
    public synchronized long nextId() {
        long currentTimestamp = currentTime();
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refuse to generate id for %d milliseconds", lastTimestamp - currentTimestamp));
        }
        if (lastTimestamp == currentTimestamp) {
            autoIncrementNum = (autoIncrementNum + 1) & maxAutoIncrementNum;
            if (autoIncrementNum == 0) {
                currentTimestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            autoIncrementNum = 0L;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - startTimestamp) << timestampLeftShift) | (dataCenterId << dataCenterIdShift) | (workerId << workerIdShift) | autoIncrementNum;
    }

    /**
     * wait until next millisecond
     *
     * @return long
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = currentTime();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTime();
        }
        return timestamp;
    }

    /**
     * get current time stamp
     *
     * @return long
     */
    protected long currentTime() {
        return System.currentTimeMillis();
    }

}