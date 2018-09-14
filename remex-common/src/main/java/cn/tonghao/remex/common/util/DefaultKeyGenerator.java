package cn.tonghao.remex.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * id生成器
 * 在分布式id(sharding-jdbc)基础上改造
 */
public class DefaultKeyGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultKeyGenerator.class);

    private static final long MAX_SEQUENCE = 100; //max sequence number

    private static final int SEQUENCE_LENGTH = 2; //sequence placeholder

    private static final long WAIT_MAX_MS = 500; // 时钟回拨可接受最大ms

    private static String workerId = ""; // default blank workId

    private SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    private long sequence = 0;

    private long lastTime = 0;

    private static int workId_length = 2;

    static {
        initWorkerId(workId_length);
    }

    private static class Singleton {
        private static DefaultKeyGenerator instance = new DefaultKeyGenerator();
    }

    private static void initWorkerId(int workIdLength) {
        LOG.info("初始化IP:", NetworkUtils.getIPv4Address());
        String ip_str_num = NetworkUtils.getIPv4Address().replace(".", "");
        if (ip_str_num.length() < workId_length) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < workId_length - ip_str_num.length(); i++) {
                sb.append("0");
            }
            ip_str_num = sb.append(ip_str_num).toString();
        }
        setWorkerId(ip_str_num.substring(ip_str_num.length() - workIdLength, ip_str_num.length()));
    }

    private static void setWorkerId(final String workerId) {
        DefaultKeyGenerator.workerId = workerId;
    }

	public static DefaultKeyGenerator getInstance() {
		return Singleton.instance;
	}

    public synchronized String generateUniqueId(String prefix, String random) {
        long currentMillis = System.currentTimeMillis();

        if (currentMillis < lastTime) { // 发生时钟回拨
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            if ((lastTime - currentMillis) < WAIT_MAX_MS) { // 时钟回拨在可接受范围内
                LOG.debug("lastTime-----{};currentMillis-----{}", sdf.format(new Date(lastTime)), sdf.format(new Date(currentMillis)));
                currentMillis = waitUntilNextTime(currentMillis);
            } else {
                LOG.debug("lastTime====={};currentMillis====={}", sdf.format(new Date(lastTime)), sdf.format(new Date(currentMillis)));
                throw new IllegalArgumentException("currentTime is less than lastTime larger than max gap");
            }
        }

        // 如果和最后一次请求处于统一毫秒，那么sequence + 1
        if (lastTime == currentMillis) {
            if (0L == (sequence = ++sequence % MAX_SEQUENCE)) {
                currentMillis = waitUntilNextTime(currentMillis); // 最大支持每1毫秒MAX_SEQUENCE次并发
            }
        } else {
            sequence = 0;
        }
        lastTime = currentMillis;

        StringBuilder sb = new StringBuilder();
        sb.append(prefix != null ? prefix : "");
        sb.append(timeFormatter.format(new Date(currentMillis)));
        sb.append(random);
        sb.append(workerId);
        String seq_str = Long.toString(sequence + MAX_SEQUENCE);
        sb.append(seq_str.substring(seq_str.length() - SEQUENCE_LENGTH, seq_str.length()));

        return sb.toString();
    }

    private long waitUntilNextTime(final long lastTime) {
        long time = System.currentTimeMillis();
        while (time <= lastTime) {
            time = System.currentTimeMillis();
        }
        return time;
    }


}
