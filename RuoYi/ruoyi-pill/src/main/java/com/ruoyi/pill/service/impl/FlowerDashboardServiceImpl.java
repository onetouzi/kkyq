package com.ruoyi.pill.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.pill.mapper.FlowerDashboardMapper;
import com.ruoyi.pill.domain.FlowerDashboardVo;
import com.ruoyi.pill.service.IFlowerDashboardService;

/**
 * 数据统计Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
@Service
public class FlowerDashboardServiceImpl implements IFlowerDashboardService 
{
    @Autowired
    private FlowerDashboardMapper flowerDashboardMapper;

    /**
     * 获取统计数据
     * 
     * @return 统计数据
     */
    @Override
    public FlowerDashboardVo selectStatistics()
    {
        return flowerDashboardMapper.selectStatistics();
    }
}

