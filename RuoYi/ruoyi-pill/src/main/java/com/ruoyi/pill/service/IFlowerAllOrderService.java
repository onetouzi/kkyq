package com.ruoyi.pill.service;

import java.util.List;
import com.ruoyi.pill.domain.FlowerAllOrder;

/**
 * 全部订单Service接口
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
public interface IFlowerAllOrderService 
{
    /**
     * 查询全部订单列表
     * 
     * @param flowerAllOrder 全部订单
     * @return 全部订单集合
     */
    public List<FlowerAllOrder> selectFlowerAllOrderList(FlowerAllOrder flowerAllOrder);
}

