package com.ruoyi.pill.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.pill.mapper.FlowerAllOrderMapper;
import com.ruoyi.pill.domain.FlowerAllOrder;
import com.ruoyi.pill.service.IFlowerAllOrderService;

/**
 * 全部订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
@Service
public class FlowerAllOrderServiceImpl implements IFlowerAllOrderService 
{
    @Autowired
    private FlowerAllOrderMapper flowerAllOrderMapper;

    /**
     * 查询全部订单列表
     * 
     * @param flowerAllOrder 全部订单
     * @return 全部订单
     */
    @Override
    public List<FlowerAllOrder> selectFlowerAllOrderList(FlowerAllOrder flowerAllOrder)
    {
        return flowerAllOrderMapper.selectFlowerAllOrderList(flowerAllOrder);
    }
}

