package com.ruoyi.pill.service;

import com.ruoyi.pill.domain.FlowerDashboardVo;

/**
 * 数据统计Service接口
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
public interface IFlowerDashboardService 
{
    /**
     * 获取统计数据
     * 
     * @return 统计数据
     */
    public FlowerDashboardVo selectStatistics();
}

