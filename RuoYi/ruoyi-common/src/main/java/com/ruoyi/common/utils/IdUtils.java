package com.ruoyi.common.utils;

/**
 * 雪花算法ID生成工具类【返回String类型】
 * 解决「无法解析符号IdUtils」+「返回String」双重问题
 */
public class IdUtils {
    /** 开始时间戳 (2025-01-01) */
    private static final long START_TIMESTAMP = 1735689600000L;

    /** 序列号位数 */
    private static final long SEQUENCE_BIT = 12;
    /** 机器标识位数 */
    private static final long MACHINE_BIT = 10;

    /** 序列号最大值 */
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    /** 机器标识最大值 */
    private static final long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);

    /** 机器标识左移位数 */
    private static final long MACHINE_LEFT = SEQUENCE_BIT;
    /** 时间戳左移位数 */
    private static final long TIMESTAMP_LEFT = SEQUENCE_BIT + MACHINE_BIT;

    /** 本机机器标识（固定为1，单机部署足够用） */
    private static final long MACHINE_ID = 1;

    private static long sequence = 0L;
    private static long lastTimestamp = -1L;

    /**
     * 生成下一个ID ✅ 核心改造：返回 String 类型
     * @return 19位数字的字符串雪花ID，例："1735701234567890123"
     */
    public static synchronized String nextId() {
        long currTimestamp = getCurrentTimestamp();
        if (currTimestamp < lastTimestamp) {
            throw new RuntimeException("时间戳异常，无法生成ID！");
        }

        if (currTimestamp == lastTimestamp) {
            // 同一毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0L) {
                // 序列号耗尽，等待下一毫秒
                currTimestamp = getNextMill();
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currTimestamp;

        // 拼接64位雪花ID（long类型）
        long snowId = (currTimestamp - START_TIMESTAMP) << TIMESTAMP_LEFT
                | MACHINE_ID << MACHINE_LEFT
                | sequence;

        // ✅ 关键：将long类型雪花ID 转为 String类型返回
        return String.valueOf(snowId);
    }

    /** 获取下一毫秒时间戳 */
    private static long getNextMill() {
        long mill = getCurrentTimestamp();
        while (mill <= lastTimestamp) {
            mill = getCurrentTimestamp();
        }
        return mill;
    }

    /** 获取当前毫秒时间戳 */
    private static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    // 测试效果（可选）
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            String id = IdUtils.nextId();
            System.out.println("生成String类型雪花ID：" + id);
            System.out.println("ID类型：" + id.getClass().getName()); // 输出：java.lang.String
        }
    }
}