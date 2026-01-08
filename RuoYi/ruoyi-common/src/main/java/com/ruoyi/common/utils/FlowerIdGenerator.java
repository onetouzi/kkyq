package com.ruoyi.common.utils;

import com.ruoyi.common.utils.IdUtils;

/**
 * 花卉编号生成工具
 */
public class FlowerIdGenerator {

    /**
     * 生成20位长度的花卉编号
     * 逻辑：前缀字符(1位) + 雪花ID(19位) = 20位
     */
    public static String generateFlowerNo() {
        String snowflakeId = IdUtils.getFlowerNextId();
        return snowflakeId;
    }
}