package com.ruoyi.pill.service.impl;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.pill.mapper.FlowerOrderMapper;
import com.ruoyi.pill.mapper.FlowerMapper;
import com.ruoyi.pill.domain.FlowerOrder;
import com.ruoyi.pill.domain.Flower;
import com.ruoyi.pill.service.IFlowerOrderService;
import com.ruoyi.common.core.text.Convert;

/**
 * 花卉订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
@Service
public class FlowerOrderServiceImpl implements IFlowerOrderService 
{
    @Autowired
    private FlowerOrderMapper flowerOrderMapper;

    @Autowired
    private FlowerMapper flowerMapper;

    /**
     * 查询花卉订单
     * 
     * @param orderId 花卉订单主键
     * @return 花卉订单
     */
    @Override
    public FlowerOrder selectFlowerOrderByOrderId(Long orderId)
    {
        return flowerOrderMapper.selectFlowerOrderByOrderId(orderId);
    }

    /**
     * 查询花卉订单列表
     * 
     * @param flowerOrder 花卉订单
     * @return 花卉订单
     */
    @Override
    public List<FlowerOrder> selectFlowerOrderList(FlowerOrder flowerOrder)
    {
        return flowerOrderMapper.selectFlowerOrderList(flowerOrder);
    }

    /**
     * 新增花卉订单
     * 
     * @param flowerOrder 花卉订单
     * @return 结果
     */
    @Override
    public int insertFlowerOrder(FlowerOrder flowerOrder)
    {
        flowerOrder.setCreateTime(DateUtils.getNowDate());
        return flowerOrderMapper.insertFlowerOrder(flowerOrder);
    }

    /**
     * 修改花卉订单
     * 
     * @param flowerOrder 花卉订单
     * @return 结果
     */
    @Override
    public int updateFlowerOrder(FlowerOrder flowerOrder)
    {
        flowerOrder.setUpdateTime(DateUtils.getNowDate());
        return flowerOrderMapper.updateFlowerOrder(flowerOrder);
    }

    /**
     * 批量删除花卉订单
     * 
     * @param orderIds 需要删除的花卉订单主键
     * @return 结果
     */
    @Override
    public int deleteFlowerOrderByOrderIds(String orderIds)
    {
        return flowerOrderMapper.deleteFlowerOrderByOrderIds(Convert.toLongArray(orderIds));
    }

    /**
     * 删除花卉订单信息
     * 
     * @param orderId 花卉订单主键
     * @return 结果
     */
    @Override
    public int deleteFlowerOrderByOrderId(Long orderId)
    {
        return flowerOrderMapper.deleteFlowerOrderByOrderId(orderId);
    }

    /**
     * 取消订单
     * 
     * @param orderId 订单ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelOrder(Long orderId)
    {
        FlowerOrder order = flowerOrderMapper.selectFlowerOrderByOrderId(orderId);
        if (order == null)
        {
            return 0;
        }
        // 恢复库存
        flowerMapper.updateFlowerNum(order.getFlowerId(), order.getQuantity());
        // 更新订单状态
        order.setStatus("CANCELLED");
        order.setUpdateTime(DateUtils.getNowDate());
        return flowerOrderMapper.updateFlowerOrder(order);
    }

    /**
     * 完成订单
     * 
     * @param orderId 订单ID
     * @return 结果
     */
    @Override
    public int completeOrder(Long orderId)
    {
        FlowerOrder order = flowerOrderMapper.selectFlowerOrderByOrderId(orderId);
        if (order == null)
        {
            return 0;
        }
        order.setStatus("COMPLETED");
        order.setUpdateTime(DateUtils.getNowDate());
        return flowerOrderMapper.updateFlowerOrder(order);
    }
}

