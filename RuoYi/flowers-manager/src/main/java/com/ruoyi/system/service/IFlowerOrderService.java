package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.FlowerOrder;

/**
 * 花卉订单Service接口
 * 
 * @author b3q
 * @date 2026-01-07
 */
public interface IFlowerOrderService 
{
    /**
     * 查询花卉订单
     * 
     * @param orderId 花卉订单主键
     * @return 花卉订单
     */
    public FlowerOrder selectFlowerOrderByOrderId(String orderId);

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

    public int insertOrders(List<FlowerOrder> orders);

    public int batchPlaceOrders(List<FlowerOrder> orders);

    public FlowerOrder selectFlowerOrderOne(String orderId, String userId, String flowerId) ;

    public int deleteFlowerOrderOne(String orderId, String userId, String flowerId) ;

    public int cancelOrder(String orderId, String userId, String flowerId);

    public int deleteFlowerOrderBatch(List<FlowerOrder> orders);
}
