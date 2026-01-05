package com.ruoyi.pill.mapper;

import java.util.List;
import com.ruoyi.pill.domain.FlowerAllOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 全部订单Mapper接口
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
@Mapper
public interface FlowerAllOrderMapper 
{
    /**
     * 查询全部订单列表
     * 
     * @param flowerAllOrder 全部订单
     * @return 全部订单集合
     */
    public List<FlowerAllOrder> selectFlowerAllOrderList(FlowerAllOrder flowerAllOrder);
}

