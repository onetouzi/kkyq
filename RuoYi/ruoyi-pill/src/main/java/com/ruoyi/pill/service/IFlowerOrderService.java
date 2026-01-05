package com.ruoyi.pill.service;

import java.util.List;
import com.ruoyi.pill.domain.FlowerOrder;

/**
 * 花卉订单Service接口
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
public interface IFlowerOrderService 
{
    /**
     * 查询花卉订单
     * 
     * @param orderId 花卉订单主键
     * @return 花卉订单
     */
    public FlowerOrder selectFlowerOrderByOrderId(Long orderId);

    /**
     * 查询花卉订单列表
     * 
     * @param flowerOrder 花卉订单
     * @return 花卉订单集合
     */
    public List<FlowerOrder> selectFlowerOrderList(FlowerOrder flowerOrder);

    /**
     * 新增花卉订单
     * 
     * @param flowerOrder 花卉订单
     * @return 结果
     */
    public int insertFlowerOrder(FlowerOrder flowerOrder);

    /**
     * 修改花卉订单
     * 
     * @param flowerOrder 花卉订单
     * @return 结果
     */
    public int updateFlowerOrder(FlowerOrder flowerOrder);

    /**
     * 批量删除花卉订单
     * 
     * @param orderIds 需要删除的花卉订单主键集合
     * @return 结果
     */
    public int deleteFlowerOrderByOrderIds(String orderIds);

    /**
     * 删除花卉订单信息
     * 
     * @param orderId 花卉订单主键
     * @return 结果
     */
    public int deleteFlowerOrderByOrderId(Long orderId);

    /**
     * 取消订单
     * 
     * @param orderId 订单ID
     * @return 结果
     */
    public int cancelOrder(Long orderId);

    /**
     * 完成订单
     * 
     * @param orderId 订单ID
     * @return 结果
     */
    public int completeOrder(Long orderId);
}

