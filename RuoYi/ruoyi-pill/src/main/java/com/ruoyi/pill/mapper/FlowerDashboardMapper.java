package com.ruoyi.pill.mapper;

import com.ruoyi.pill.domain.FlowerDashboardVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据统计Mapper接口
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
@Mapper
public interface FlowerDashboardMapper 
{
    /**
     * 获取统计数据
     * 
     * @return 统计数据
     */
    public FlowerDashboardVo selectStatistics();
}

